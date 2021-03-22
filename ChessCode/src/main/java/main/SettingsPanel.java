package main;

import java.awt.Color;
import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SettingsPanel extends JFrame {

	private static final long serialVersionUID = 2369329972406971395L;

	private Controller controller;
	
	JTextField seed;
	
	JTextField output;

	public SettingsPanel() {
		super("Settings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		setFields();

		setSize(750, 700);
		setLayout(null);
	}

	private void setFields() {
		setFilePicker();
		setSeedInput();
		setCreateAlphabet();
		setInputText();
		setOutputText();
	}

	private void setFilePicker() {
		JFileChooser file = new JFileChooser();
		file.setBounds(10, 10, 400, 400);
		file.setFileSelectionMode(JFileChooser.FILES_ONLY);
		file.addActionListener(event -> {
			if (file.getSelectedFile().isFile()) {
				File f = file.getSelectedFile();
				String mimetype = new MimetypesFileTypeMap().getContentType(f);
				String type = mimetype.split("/")[0];
				if (type.equals("image")) {
					// TODO
				}
			}
		});

		add(file);
	}

	private void setSeedInput() {
		JLabel title = new JLabel("Seed");
		title.setBounds(10, 350 + 70, 200, 30);

		seed = new JTextField();
		seed.addActionListener(event -> {
			// TODO
		});
		seed.setBounds(10, 380 + 70, 200, 30);

		JLabel seedError = new JLabel("Invalid Input");
		seedError.setBounds(10, 410 + 80, 200, 30);
		seedError.setForeground(Color.RED);
		seedError.setVisible(false);

		add(title);
		add(seed);
		add(seedError);
	}

	private void setInputText() {
		JLabel title = new JLabel("Texto a encriptar");
		title.setBounds(10, 350 + 130, 200, 30);

		JTextField input = new JTextField();
		input.setBounds(10, 380 + 130, 400, 90);
		
		JButton encrypt = new JButton("Encriptar");
		encrypt.setBounds(10, 380 + 230, 200, 30);
		encrypt.addActionListener(event -> {
			output.setText(controller.encrypt(input.getText()));
		});

		add(title);
		add(input);
		add(encrypt);
	}

	private void setOutputText() {
		JLabel title = new JLabel("Texto encriptado");
		title.setBounds(410, 350 + 130, 200, 30);

		output = new JTextField();
		output.setBounds(410, 380 + 130, 400, 90);

		add(title);
		add(output);
	}

	private void setCreateAlphabet() {
		JButton button = new JButton("Crear Alfabeto");
		button.setBounds(220, 350 + 70, 200, 30);
		button.addActionListener(event -> {
			try {
				controller.createAlphabet();
				seed.setText(Long.toString(controller.getSeed()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		add(button);
	}

	public static void main(String[] args) {
		new SettingsPanel();
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}
