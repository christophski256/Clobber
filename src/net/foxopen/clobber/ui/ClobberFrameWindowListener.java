package net.foxopen.clobber.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.apache.log4j.Logger;

import net.foxopen.clobber.ClobberModel;

public class ClobberFrameWindowListener implements WindowListener {

	private ClobberModel model;
	private Logger logger;
	
	public ClobberFrameWindowListener(ClobberModel model) {
		this.model = model;
		this.logger = Logger.getLogger(this.getClass());
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// Don't care

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// Don't care

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.logger.info("Window close detected");
		this.model.shutdown();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// Don't care

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		//Don't Care

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		//Don't Care

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		//Don't Care

	}

}
