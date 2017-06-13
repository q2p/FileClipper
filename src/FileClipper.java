import javax.swing.*;
import java.io.*;

public final class FileClipper {
	public static void main(final String[] args) {
		final File firstFile = readFile("Select first file");
		if(firstFile == null)
			return;

		final File secondFile = readFile("Select second file");
		if(secondFile == null)
			return;

		final File outFile = writeFile();
		if(outFile == null)
			return;

		final String message = hardWork(firstFile, secondFile, outFile);
		if(message == null)
			JOptionPane.showMessageDialog(null, "Process finished.", "Done", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private static String hardWork(final File firstFile, final File secondFile, final File outFile) {
		final byte[] buff = new byte[32*1024];

		try(FileOutputStream out = new FileOutputStream(outFile); FileInputStream fi = new FileInputStream(firstFile); FileInputStream si = new FileInputStream(secondFile)) {
			connect(out, buff, fi);
			connect(out, buff, si);
		} catch(Exception e) {
			return e.getMessage();
		}

		return null;
	}

	private static void connect(final FileOutputStream out, final byte[] buff, final FileInputStream in) throws IOException {
		int pointer;
		while(true) {
			pointer = in.read(buff);

			if(pointer == -1)
				return;

			out.write(buff, 0, pointer);
			out.flush();
		}
	}

	private static File writeFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle("Select output file");
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		fc.setApproveButtonText("Select");
		if(fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return null;

		return fc.getSelectedFile();
	}

	private static File readFile(final String header) {
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle(header);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setApproveButtonText("Select");
		if(fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return null;

		return fc.getSelectedFile();
	}
}
