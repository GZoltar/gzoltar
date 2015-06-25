package pt.up.fe.aes.maven.messaging;

import pt.up.fe.aes.base.events.EventListener;
import pt.up.fe.aes.base.events.MultiEventListener;
import pt.up.fe.aes.base.messaging.Service;
import pt.up.fe.aes.base.spectrum.SpectrumBuilder;
import pt.up.fe.aes.maven.AbstractAESMojo;

public class ServiceHandler implements Service {

	private final AbstractAESMojo mojo;
	
	private SpectrumBuilder spectrumBuilder;
	private EventListener listener;
	
	public ServiceHandler(AbstractAESMojo mojo) {
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
