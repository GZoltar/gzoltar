package pt.up.fe.ddu.maven.messaging;

import pt.up.fe.ddu.base.events.EventListener;
import pt.up.fe.ddu.base.events.MultiEventListener;
import pt.up.fe.ddu.base.messaging.Service;
import pt.up.fe.ddu.base.spectrum.SpectrumBuilder;
import pt.up.fe.ddu.maven.AbstractDDUMojo;

public class ServiceHandler implements Service {

	private final AbstractDDUMojo mojo;
	
	private SpectrumBuilder spectrumBuilder;
	private EventListener listener;
	
	public ServiceHandler(AbstractDDUMojo mojo) {
		this.mojo = mojo;
		spectrumBuilder = new SpectrumBuilder();
		listener = new MultiEventListener(spectrumBuilder);
	}
	
	@Override
	public EventListener getEventListener() {
		return listener;
	}

	@Override
	public void interrupted() {
		
	}

	@Override
	public void terminated() {
		mojo.storeCurrentSpectrum(spectrumBuilder.getSpectrum());
	}

}
