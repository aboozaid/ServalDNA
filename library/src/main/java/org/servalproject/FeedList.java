package org.servalproject;

import org.servalproject.servaldna.HttpJsonSerialiser;
import org.servalproject.servaldna.ServalDInterfaceException;
import org.servalproject.servaldna.Subscriber;
import org.servalproject.servaldna.meshmb.MeshMBCommon;
import org.servalproject.servaldna.rhizome.RhizomeBundleList;
import org.servalproject.servaldna.rhizome.RhizomeListBundle;

import java.io.IOException;

/**
 * Created by jeremy on 11/10/16.
 */
public class FeedList extends AbstractFutureList<RhizomeListBundle, IOException> {
	private static final String TAG = "FeedList";
	public final String search;

	public FeedList(Serval serval, String search) {
		super(serval);
		this.search = search;
	}

	@Override
	protected void start() {
		if (last == null && hasMore)
			return;
		super.start();
	}

	@Override
	protected HttpJsonSerialiser<RhizomeListBundle, IOException> openPast() throws ServalDInterfaceException, IOException {
		RhizomeBundleList list = new RhizomeBundleList(serval.getResultClient());
		list.setServiceFilter(MeshMBCommon.SERVICE);
		if (search!=null)
			list.setNameFilter(search);
		list.connect();
		return list;
	}

	@Override
	protected HttpJsonSerialiser<RhizomeListBundle, IOException> openFuture() throws ServalDInterfaceException, IOException {
		RhizomeBundleList list = new RhizomeBundleList(serval.getResultClient(), last == null? "" : last.token);
		list.setServiceFilter(MeshMBCommon.SERVICE);
		if (search!=null && !"".equals(search))
			list.setNameFilter(search);
		list.connect();
		return list;
	}

	private void updatePeer(RhizomeListBundle item) {
		// TODO verify that the sender and id are for the same identity!
		// for now we can assume this, but we might break this rule in a future version
		if (item.author == null && item.manifest.sender == null)
			return;
		Subscriber subscriber = new Subscriber(
				item.author != null ? item.author : item.manifest.sender,
				item.manifest.id, true);
		Peer p = serval.knownPeers.getPeer(subscriber);
		p.updateFeedName(item.manifest.name);
	}

	@Override
	protected void addingFutureItem(RhizomeListBundle item) {
		updatePeer(item);
		super.addingFutureItem(item);
	}

	@Override
	protected void addingPastItem(RhizomeListBundle item) {
		if (item != null)
			updatePeer(item);
		super.addingPastItem(item);
	}
}
