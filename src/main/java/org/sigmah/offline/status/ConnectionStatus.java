package org.sigmah.offline.status;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.event.EventBus;
import org.sigmah.client.page.RequestParameter;
import org.sigmah.client.ui.zone.Zone;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.offline.sync.StateListener;
import org.sigmah.offline.sync.Synchronizer;

/**
 * Detect changes between online and offline mode.
 * 
 * @author Raphaël Calabro <rcalabro@ideia.fr>
 */
@Singleton
public class ConnectionStatus {
	/**
	 * Listener interface. Called immediately on add and after every status 
	 * changes.
	 */
	public interface Listener {
		void connectionStatusHasChanged(ApplicationState state);
	}
	
    private final Synchronizer synchronizer;
    
	private boolean online;
    private ApplicationState state;
	private final List<Listener> listeners;
	private final Timer timer;

	@Inject
	public ConnectionStatus(EventBus eventBus, Synchronizer synchronizer) {
		this.online = getInitialStatus();
        this.state = online ? ApplicationState.ONLINE : ApplicationState.OFFLINE;
		this.listeners = new ArrayList<Listener>();
        this.synchronizer = synchronizer;
		this.timer = new Timer() {
			@Override
			public void run() {
				updateStatus();
			}
		};
		
		initialize(eventBus);
        synchronizer.setConnectionStatus(this);
	}
	
	public static native boolean getInitialStatus() /*-{
		return typeof $wnd.online == 'undefined' || $wnd.online;
	}-*/;
	
	private void initialize(final EventBus eventBus) {
		// Base listener
		addListener(new Listener() {
			@Override
			public void connectionStatusHasChanged(ApplicationState state) {
                final boolean canContactServer = state == ApplicationState.ONLINE;
                
				eventBus.updateZoneRequest(Zone.OFFLINE_BANNER.requestWith(RequestParameter.CONTENT, state));
				
				if(canContactServer) {
					RootPanel.getBodyElement().removeClassName("offline");
				} else {
					RootPanel.getBodyElement().addClassName("offline");
				}
			}
		});
		
		if(GWT.isProdMode()) {
			startAutoRefresh();
		}
	}
	
	public void updateStatus() {
		final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "sigmah/online.nocache.json");
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(com.google.gwt.http.client.Request request, Response response) {
					if(response != null && response.getText() != null && !response.getText().isEmpty()) {
						try {
							final JSONValue value = JSONParser.parseStrict(response.getText());
							final JSONObject object = value.isObject();
							if(object != null) {
								final JSONValue online = object.get("online");
								final JSONBoolean isOnline = online.isBoolean();
								setOnline(isOnline != null && isOnline.booleanValue());
							} else {
								setOnline(false);
							}
						} catch(JSONException ex) {
							setOnline(false);
							Log.error("An error occured while parsing the JSON string: '" + response.getText() + "'.", ex);
						}
					} else {
						setOnline(false);
					}
				}
				
				@Override
				public void onError(com.google.gwt.http.client.Request request, Throwable exception) {
					setOnline(false);
				}
			});
			
		} catch (RequestException ex) {
			setOnline(false);
			Log.error("An error occured while checking the connection state.", ex);
		}
	}
	
	private void setOnline(boolean status) {
		if(this.online != status) {
			this.online = status;
			
            synchronizer.getApplicationState(status, new StateListener() {

                @Override
                public void onStateKnown(ApplicationState state) {
                    setState(state);
                }
            });
		}
	}

	public boolean isOnline() {
		return online;
	}

    public void setState(ApplicationState state) {
        this.state = state;
        fireStatusHasChanged();
    }

    public ApplicationState getState() {
        return state;
    }
    
	public void addListener(final Listener listener) {
		listeners.add(listener);
		
        listener.connectionStatusHasChanged(state);
		synchronizer.getApplicationState(online, new StateListener() {

            @Override
            public void onStateKnown(ApplicationState state) {
                setState(state);
            }
        });
	}
	
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	protected void fireStatusHasChanged() {
		for(final Listener listener : listeners) {
			listener.connectionStatusHasChanged(state);
		}
	}
	
	public void startAutoRefresh() {
		timer.scheduleRepeating(3000);
	}
	
	public void stopAutoRefresh() {
		timer.cancel();
	}
}
