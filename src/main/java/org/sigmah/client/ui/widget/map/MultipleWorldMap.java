package org.sigmah.client.ui.widget.map;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sigmah.shared.dto.BoundingBoxDTO;

/**
 * Display every given WorldMap implementation in a tab panel.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class MultipleWorldMap implements WorldMap {
	
	private final WorldMap[] implementations;
	private final TabPanel tabPanel;
	
	private boolean useCenterAndZoom;
	private BoundingBoxDTO bounds;
	private double centerLongitude;
	private double centerLatitude;
	private int zoom;

	private boolean wasPanned;
	private double pannedLongitude;
	private double pannedLatitude;
	
	public MultipleWorldMap(WorldMap... worldMaps) {
		tabPanel = new TabPanel();
		implementations = worldMaps;
		
		int index = 0;
		for(final WorldMap worldMap : worldMaps) {
			final TabItem item = new TabItem(worldMap.getName());
			item.setLayout(new FitLayout());
			item.add(worldMap.asWidget());
			item.setData("index", index++);
			tabPanel.add(item);
		}
		
		tabPanel.addListener(Events.Select, new Listener<TabPanelEvent>() {

			@Override
			public void handleEvent(final TabPanelEvent be) {
				new Timer() {
					@Override
					public void run() {
						replicateView(be);
					}
				}.schedule(500);
			}
		});
	}
	
	private void replicateView(TabPanelEvent be) {
		final int index = be.getItem().getData("index");
		final WorldMap worldMap = implementations[index];

		if(useCenterAndZoom) {
			worldMap.setCenterAndZoom(centerLatitude, centerLongitude, zoom);
		} else if(bounds != null) {
			worldMap.setBounds(bounds);
		}

		if(wasPanned) {
			worldMap.panTo(pannedLatitude, pannedLongitude);
		}
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException("Not supported by this implementation."); 
	}

	@Override
	public void addPin(Pin pin) {
		for(final WorldMap implementation : implementations) {
			implementation.addPin(pin);
		}
	}

	@Override
	public void setPins(List<Pin> pins) {
		final BoundingBoxDTO pinBounds = new BoundingBoxDTO(0, 0, 0, 0);
		if(!pins.isEmpty()) {
			final Pin pin = pins.get(0);
			pinBounds.setX1(pin.getLongitude());
			pinBounds.setX2(pin.getLongitude());
			pinBounds.setY1(pin.getLatitude());
			pinBounds.setY2(pin.getLatitude());
		}
		
		for(final Pin pin : pins) {
			// Extends the bounding box to cover all the pins.
			pinBounds.grow(pin.getLongitude(), pin.getLatitude());
		}
		
		this.useCenterAndZoom = false;
		this.bounds = pinBounds;
		
		for(final WorldMap implementation : implementations) {
			implementation.setPins(pins);
		}
	}

	@Override
	public void updatePinPosition(Pin pin) {
		for(final WorldMap implementation : implementations) {
			implementation.updatePinPosition(pin);
		}
	}

	@Override
	public void removeAllPins() {
		for(final WorldMap implementation : implementations) {
			implementation.removeAllPins();
		}
	}

	@Override
	public void addPinDragEndHandler(Pin pin, PinDragEndHandler dragEndHandler) {
		for(final WorldMap implementation : implementations) {
			implementation.addPinDragEndHandler(pin, dragEndHandler);
		}
	}

	@Override
	public void setBounds(BoundingBoxDTO bounds) {
		this.useCenterAndZoom = false;
		this.wasPanned = false;
		this.bounds = bounds;
		
		for(final WorldMap implementation : implementations) {
			implementation.setBounds(bounds);
		}
	}

	@Override
	public BoundingBoxDTO getBounds() {
		for(final WorldMap implementation : implementations) {
			return implementation.getBounds();
		}
		return null;
	}

	@Override
	public void setCenterAndZoom(double latitude, double longitude, int zoom) {
		this.useCenterAndZoom = true;
		this.wasPanned = false;
		this.centerLatitude = latitude;
		this.centerLongitude = longitude;
		this.zoom = zoom;
		
		for(final WorldMap implementation : implementations) {
			implementation.setCenterAndZoom(latitude, longitude, zoom);
		}
	}

	@Override
	public void panTo(double latitude, double longitude) {
		this.wasPanned = true;
		this.pannedLatitude = latitude;
		this.pannedLongitude = latitude;
		
		for(final WorldMap implementation : implementations) {
			implementation.panTo(latitude, longitude);
		}
	}

	@Override
	public void setSize(String width, String height) {
		tabPanel.setWidth(width);
		tabPanel.setHeight(height);
		
		for(final WorldMap implementation : implementations) {
			implementation.setSize("100%", "100%");
		}
	}

	@Override
	public Widget asWidget() {
		return tabPanel;
	}

	@Override
	public void setLoading(boolean loading) {
		for(final WorldMap implementation : implementations) {
			implementation.setLoading(loading);
		}
	}

	@Override
	public boolean isLoading() {
		for(final WorldMap implementation : implementations) {
			return implementation.isLoading();
		}
		return false;
	}

	
}
