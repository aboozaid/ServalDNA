package org.servalproject;

import org.servalproject.servaldna.ServalDInterfaceException;
import org.servalproject.servaldna.meshms.MeshMSException;

import java.io.IOException;

/**
 * Created by jeremy on 11/10/16.
 */
public class IdentityFeed extends MessageFeed {
	public final Identity id;

	IdentityFeed(Serval serval, Identity id) {
		super(serval, id.subscriber);
		this.id = id;
	}

	public void sendMessage(String message) throws ServalDInterfaceException, IOException, MeshMSException {
		if (serval.uiHandler.isOnThread())
			throw new IllegalStateException();
		serval.getResultClient().meshmbSendMessage(id.subscriber.signingKey, message);
	}

}
