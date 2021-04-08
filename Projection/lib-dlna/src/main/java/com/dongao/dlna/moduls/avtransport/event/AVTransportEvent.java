package com.dongao.dlna.moduls.avtransport.event;

import com.dongao.dlna.moduls.avtransport.entity.AVTransportInfo;
import com.dongao.dlna.upnp.UpnpSubscriptionCallback;
import com.dongao.dlna.utils.ObjectParse;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.lastchange.LastChangeParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hubing
 * @version 1.0.0 2015-5-6
 */

public abstract class AVTransportEvent extends UpnpSubscriptionCallback {

	public AVTransportEvent(Service service) {
		super(service);
	}

	public AVTransportEvent(Service service, int requestedDurationSeconds) {
		super(service, requestedDurationSeconds);
	}
	
	@Override
	public LastChangeParser getLastChangeParser() {
		return new AVTransportLastChangeParser();
	}

	@Override
	public void onReceive(List<EventedValue> values) {
		Map<String, Object> currStates = new HashMap<String, Object>();
		for (EventedValue ev : values) {
			currStates.put(ev.getName(), ev.getValue());
		}
		AVTransportInfo avtInfo = ObjectParse.parseMapToObject(currStates, AVTransportInfo.class);
		if (avtInfo == null) {
			avtInfo = new AVTransportInfo();
		}
		received(avtInfo);
	}

	public abstract void received(AVTransportInfo avTransportInfo);
	
}

