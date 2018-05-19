package rfid_communication;

import java.io.IOException;

import javax.swing.JTextField;

import user_interface.MainWindow;
import user_interface.MessageWindow;

import com.phidgets.*;
import com.phidgets.event.*;

public class RFIDTag {
	private static volatile RFIDTag instance;

	private RFIDPhidget rfid;
	private TagGainListener tagListener;

	// private JTextField textField_RFID;

	public void setTagGainListener(TagGainListener listener) {
		if (tagListener != null) {
			rfid.removeTagGainListener(tagListener);
		}
		rfid.addTagGainListener(listener);
		tagListener = listener;
	}

	public void setPrintListener() {
		setTagGainListener(new TagGainListener() {
			public void tagGained(TagGainEvent oe) {
				System.out.println("Tag Gained: " + oe.getValue() + " (Proto:"
						+ oe.getProtocol() + ")");
			}
		});
	}

	public void setMainListener(final MainWindow window) {
		setTagGainListener(new TagGainListener() {
			public void tagGained(TagGainEvent oe) {
				System.out.println("Tag Gained: " + oe.getValue() + " (Proto:"
						+ oe.getProtocol() + ")");
				if (window != null) {
					window.newTagFound(oe.getValue());
				} else {
					new MainWindow().newTagFound(oe.getValue());
				}
			}
		});
	}

	public void setTextFieldListener(final JTextField textfield) {
		setTagGainListener(new TagGainListener() {
			public void tagGained(TagGainEvent oe) {
				System.out.println("Tag Gained: " + oe.getValue() + " (Proto:"
						+ oe.getProtocol() + ")");
				textfield.setText(oe.getValue());
			}
		});
	}

	public void setMessageWindowListener(final MessageWindow window) {
			setTagGainListener(new TagGainListener() {
				public void tagGained(TagGainEvent oe) {
					System.out.println("Tag Gained: " + oe.getValue() + " (Proto:"
							+ oe.getProtocol() + ")");
					//if (window != null) {
						window.newTagFound(oe.getValue());
					//} else {
					//	new MessageWindow().newTagFound(oe.getValue());
					//}
				}
			});
		}

	public static RFIDTag getRFID() {
		if (instance == null) {
			synchronized (RFIDTag.class) {
				if (instance == null) {
					instance = new RFIDTag();
				}
			}
		}
		return instance;
	}

	private RFIDTag() {
		try {
			rfid = new RFIDPhidget();
			rfid.addAttachListener(new AttachListener() {
				public void attached(AttachEvent ae) {
					try {
						((RFIDPhidget) ae.getSource()).setAntennaOn(true);
						((RFIDPhidget) ae.getSource()).setLEDOn(true);
					} catch (PhidgetException ex) {
					}
					System.out.println("attachment of " + ae);

				}
			});
			rfid.addDetachListener(new DetachListener() {
				public void detached(DetachEvent ae) {
					System.out.println("detachment of " + ae);

				}
			});
			rfid.addErrorListener(new ErrorListener() {
				public void error(ErrorEvent ee) {
					System.out.println("error event for " + ee);
				}
			});
			rfid.openAny();
			System.out.println("waiting for RFID attachment...");
			rfid.waitForAttachment(10000);

			System.out.println("Serial: " + rfid.getSerialNumber());
			System.out.println("Outputs: " + rfid.getOutputCount());

		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	}

	// public lala() {
	// rfid = new RFIDPhidget();
	// textField_RFID=t;
	// rfid.addAttachListener(new AttachListener() {
	// public void attached(AttachEvent ae) {
	// try {
	// ((RFIDPhidget) ae.getSource()).setAntennaOn(true);
	// ((RFIDPhidget) ae.getSource()).setLEDOn(true);
	// } catch (PhidgetException ex) {
	// }
	// System.out.println("attachment of " + ae);
	//
	// }
	// });
	// rfid.addDetachListener(new DetachListener() {
	// public void detached(DetachEvent ae) {
	// System.out.println("detachment of " + ae);
	//
	// }
	// });
	// rfid.addErrorListener(new ErrorListener() {
	// public void error(ErrorEvent ee) {
	// System.out.println("error event for " + ee);
	// }
	// });
	//
	//
	//
	// rfid.addTagGainListener(new TagGainListener() {
	// public void tagGained(TagGainEvent oe) {
	// System.out.println("Tag Gained: " + oe.getValue() + " (Proto:"
	// + oe.getProtocol() + ")");
	//
	// String textfield= oe.getValue();
	// //new NewMedicine(textfield).setVisible(true);
	// if((textField_RFID!=null)){
	// textField_RFID.setText(oe.getValue());
	// }
	// else {
	// System.out.println(oe.getValue());
	// }
	//
	//
	// }});
	//
	// rfid.openAny();
	// System.out.println("waiting for RFID attachment...");
	// rfid.waitForAttachment(10000);
	//
	// System.out.println("Serial: " + rfid.getSerialNumber());
	// System.out.println("Outputs: " + rfid.getOutputCount());
	//
	// // How to write a tag:
	// // rfid.write("A TAG!!", RFIDPhidget.PHIDGET_RFID_PROTOCOL_PHIDGETS,
	// // false);
	//
	// //System.in.read();
	//
	// //rfid.close();
	// //rfid.setAntennaOn(false);
	//
	// //rfid = null;
	//
	// //System.out.println("gogog " );
	// return rfid;
	//
	// }
	//
	public static void main(String[] args) {
		RFIDTag tag = RFIDTag.getRFID();

		tag.setPrintListener();
	}

}
