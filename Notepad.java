import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.RenderingHints.Key;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class Notepad extends JFrame implements ActionListener {

	
	//�������������
	JPanel pl = new JPanel();
	JTextArea myarea = new JTextArea();

	
	private String filename; // �򿪵��ļ���
	String textContent = "";// �༭���е�����
	
	UndoManager undoManager = new UndoManager();// ����������

	public Notepad() {
		initComponment();// ����ʼ��
	}

	private void initComponment() {
		// �˵���
		JMenuBar mb = new JMenuBar();

		// �����˵�
		final JPopupMenu myPopMenu = new JPopupMenu();
		JMenuItem copy_pop = new JMenuItem("����");
		JMenuItem cut_pop = new JMenuItem("����");
		JMenuItem paste_pop = new JMenuItem("ճ��");
		JMenuItem delete_pop = new JMenuItem("ɾ��");
		JMenuItem exit_pop = new JMenuItem("���");

		myPopMenu.add(cut_pop);
		myPopMenu.add(copy_pop);
		myPopMenu.add(delete_pop);
		myPopMenu.add(paste_pop);
		myPopMenu.add(exit_pop);

		// �󶨼�����
		cut_pop.addActionListener(this);
		copy_pop.addActionListener(this);
		delete_pop.addActionListener(this);
		paste_pop.addActionListener(this);
		exit_pop.addActionListener(this);

		// �˵�
		JMenu file = new JMenu("�ļ�");
		JMenu edit = new JMenu("�༭");
		JMenu about = new JMenu("����");

		// �Ӳ˵�
		JMenuItem new_file = new JMenuItem("�½�");
		JMenuItem open = new JMenuItem("��");
		JMenuItem save = new JMenuItem("����");
		JMenuItem save_as = new JMenuItem("���Ϊ");
		JMenuItem exit = new JMenuItem("�˳�");

		JMenuItem copy = new JMenuItem("����");
		JMenuItem cut = new JMenuItem("����");
		JMenuItem paste = new JMenuItem("ճ��");
		JMenuItem delete = new JMenuItem("ɾ��");
		JMenuItem search = new JMenuItem("���Һ��滻");
		
		JMenuItem aboutsoft = new JMenuItem("�������");

		// �󶨼����¼�
		aboutsoft.addActionListener(this);
		
		new_file.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		save_as.addActionListener(this);
		exit.addActionListener(this);

		copy.addActionListener(this);
		cut.addActionListener(this);
		paste.addActionListener(this);
		delete.addActionListener(this);
		search.addActionListener(this);
		
		
		

		// ���˵�����Ӧ���Ӳ˵���ӵ��˵���
		mb.add(file);
		mb.add(edit);
		mb.add(about);

		file.add(open);
		file.add(new_file);
		file.add(save);
		file.add(save_as);
		file.add(exit);

		edit.add(copy);
		edit.add(cut);
		edit.add(paste);
		edit.add(delete);
		edit.add(search);
		
		about.add(aboutsoft);

		// ���ı�������ӹ�����
		myarea.add(myPopMenu);
		JScrollPane scrollpane = new JScrollPane(myarea);
		add(scrollpane);
		// ������
		setTitle("���±�");
		setSize(600, 400);
		setLocation(400, 300);
		// ��Ӳ˵���
		setJMenuBar(mb);
		
		
	

		// ���ڼ���
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if(!myarea.getText().equals(textContent)) 
				{
					int result = JOptionPane.showConfirmDialog(null, "�ļ������Ѹı䣬ȷ�ϱ����˳���", "����", JOptionPane.YES_NO_OPTION);
					switch (result) {
					case JOptionPane.NO_OPTION:
						System.exit(0);
						break;
					case JOptionPane.YES_OPTION:
						save();
						System.exit(0);
						break;
					default:
						break;
					}
				}
				else {
					System.exit(0);
				}
			}
		});
		
		//���̼���
		myarea.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent ke) 
			{
				
				//ctrl+fʵ�ֲ��ҹ���
				 if ((ke.getKeyCode() == KeyEvent.VK_F)   
                         && (ke.isControlDown())) 
				 { 
					 	 // ���ҶԻ���
						JDialog search = new JDialog(Notepad.this, "���Һ��滻");
						search.setSize(200, 100);
						search.setLocation(450, 350);
						JLabel label_1 = new JLabel("���ҵ�����");
						JLabel label_2 = new JLabel("�滻������");
						final JTextField textField_1 = new JTextField(5);
						final JTextField textField_2 = new JTextField(5);
						JButton buttonFind = new JButton("����");
						JButton buttonChange = new JButton("�滻");
						JPanel panel = new JPanel(new GridLayout(2, 3));
						panel.add(label_1);
						panel.add(textField_1);
						panel.add(buttonFind);
						panel.add(label_2);
						panel.add(textField_2);
						panel.add(buttonChange);
						search.add(panel);
						search.setVisible(true);
						
						// Ϊ������һ�� ��ť�󶨼����¼�
						buttonFind.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								String findText = textField_1.getText();// ���ҵ��ַ���

								String textArea = myarea.getText();// ��ǰ�ı��������
								start = textArea.indexOf(findText, end);
								end = start + findText.length();
								if (start == -1)// û���ҵ�
								{
									JOptionPane.showMessageDialog(null, "��"+findText+"��"+"�Ѿ��������", "���±�", JOptionPane.WARNING_MESSAGE);
									myarea.select(start, end);
								} else {
									myarea.select(start, end);
								}

							}
						});
						// Ϊ�滻��ť�󶨼���ʱ��
						buttonChange.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								String changeText = textField_2.getText();// �滻���ַ���
								myarea.select(start, end);
								myarea.replaceSelection(changeText);
								myarea.select(start, end);
							}
						});
					 
				 }
				 //esc�˳�
				if (ke.getKeyCode()==KeyEvent.VK_ESCAPE) 
				{
					if(!myarea.getText().equals(textContent)) 
					{
						int result = JOptionPane.showConfirmDialog(null, "�ļ������Ѹı䣬ȷ�ϱ����˳���", "����", 1);
						switch (result) {
						case JOptionPane.NO_OPTION:
							System.exit(0);
							break;
						case JOptionPane.YES_OPTION:
							save();
							System.exit(0);
							break;
						case JOptionPane.CANCEL_OPTION:
							break;
						default:
							break;
						}
					}
				}
				
				

			}
		});

		// ������
		myarea.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int mods = e.getModifiers();
				// ����Ҽ�
				if ((mods & InputEvent.BUTTON3_MASK) != 0) {
					// �����˵�
					myPopMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});

	}

	
	// ��ر���
		int start = 0;// ���ҿ�ʼλ��
		int end = 0;// ���ҽ���λ��
		
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			
			 if (e.getActionCommand() == "�½�") {
				myarea.setText("");
			} else if (e.getActionCommand() == "��") {
				FileDialog fileDialog = new FileDialog(this, "���ļ�", FileDialog.LOAD);
				fileDialog.setFile("*.txt");
				fileDialog.setVisible(true);
				if (fileDialog.getFile() != null) {
					filename = fileDialog.getDirectory() + fileDialog.getFile();// ����ļ���

					// ��ȡ�ļ�

					FileReader file_reader = new FileReader(filename);// �˴�����Ҫ�����쳣
					BufferedReader br = new BufferedReader(file_reader);
					String temp = "";
					while (br.ready())// �жϻ������Ƿ�Ϊ�գ��ǿ�ʱ����true
					{
						int c = br.read();
						temp = temp+ (char)c;
					}
					myarea.setText(temp);
					br.close();
					file_reader.close();
					textContent = myarea.getText();
					setTitle("���±�-" + filename);

				}
			} else if (e.getActionCommand() == "����") {
				save();
			} else if (e.getActionCommand() == "���Ϊ") {
				otherSave();
			} else if (e.getActionCommand() == "�˳�") {
				if(!myarea.getText().equals(textContent)) 
				{
					int result = JOptionPane.showConfirmDialog(null, "�ļ������Ѹı䣬ȷ�ϱ����˳���", "����", 1);
					switch (result) {
					case JOptionPane.NO_OPTION:
						System.exit(0);
						break;
					case JOptionPane.YES_OPTION:
						save();
						System.exit(0);
						break;
					case JOptionPane.CANCEL_OPTION:
						break;
					default:
						break;
					}
				}
					else {
						System.exit(0);
					
				}
			} else if (e.getActionCommand() == "���Һ��滻") {
				// ���ҶԻ���
				JDialog search = new JDialog(this, "���Һ��滻");
				search.setSize(200, 100);
				search.setLocation(450, 350);
				JLabel label_1 = new JLabel("���ҵ�����");
				JLabel label_2 = new JLabel("�滻������");
				final JTextField textField_1 = new JTextField(5);
				final JTextField textField_2 = new JTextField(5);
				JButton buttonFind = new JButton("����");
				JButton buttonChange = new JButton("�滻");
				JPanel panel = new JPanel(new GridLayout(2, 3));
				panel.add(label_1);
				panel.add(textField_1);
				panel.add(buttonFind);
				panel.add(label_2);
				panel.add(textField_2);
				panel.add(buttonChange);
				search.add(panel);
				search.setVisible(true);

				
				
				// Ϊ������һ�� ��ť�󶨼����¼�
				buttonFind.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String findText = textField_1.getText();// ���ҵ��ַ���

						String textArea = myarea.getText();// ��ǰ�ı��������
						start = textArea.indexOf(findText, end);
						end = start + findText.length();
						// û���ҵ�
						if (start == -1)
						{
							JOptionPane.showMessageDialog(null, "��"+findText+"��"+"�Ѿ��������", "���±�", JOptionPane.WARNING_MESSAGE);
							myarea.select(start, end);
						} else {
							myarea.select(start, end);
						}

					}
				});
				// Ϊ�滻��ť�󶨼����¼�
				buttonChange.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String changeText = textField_2.getText();// �滻���ַ���
						myarea.select(start, end);
						myarea.replaceSelection(changeText);
						myarea.select(start, end);
					}
				});

			} else if (e.getActionCommand() == "����") {
				copy();
			} else if (e.getActionCommand() == "ճ��") {
				paste();
			} else if (e.getActionCommand() == "����") {
				cut();
			} else if (e.getActionCommand() == "ɾ��") {
				delete();
			} else if (e.getActionCommand() == "�������") {
					JOptionPane.showMessageDialog(null,"����ΰ��Ʊ�д","�����Ϣ",JOptionPane. INFORMATION_MESSAGE);
			}else if (e.getActionCommand() == "���") 
			{

					int result = JOptionPane.showConfirmDialog(null, "ȷ���������������", "����", 1);
					if (result == JOptionPane.OK_OPTION) {
//						myarea.replaceRange(null,0,textContent.length());
						myarea.setText(null);
				}


				
			}
			 
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	

	
	
	
	//����
	private void save() {
		
		
		if (filename != null) 
		{
			try {
				File file = new File(filename);
				FileWriter file_writer = new FileWriter(file);
				//���ļ��������װ��������
				BufferedWriter bw = new BufferedWriter(file_writer);
				PrintWriter pw = new PrintWriter(bw);

				pw.print(myarea.getText());
				textContent = myarea.getText();
				pw.close();
				bw.close();
				file_writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			otherSave();
		}
		
	}
	
	
	//���Ϊ
	private void otherSave() 
	{
		FileDialog fileDialog = new FileDialog(this, "���Ϊ", FileDialog.SAVE);
		fileDialog.setFile("*.txt");
		fileDialog.setVisible(true);
		if (fileDialog.getFile() != null) {
			// д���ļ�
			FileWriter fw;
			try {
			fw = new FileWriter(fileDialog.getDirectory() + fileDialog.getFile());
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print(myarea.getText());
			textContent = myarea.getText();
			pw.close();
			bw.close();
			fw.close();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	//����
	private void cut() {
		copy();
		delete();
	}
    //����
	private void copy() {
		if (myarea.getSelectedText() == null) {
			JOptionPane.showMessageDialog(null, "��û��ѡ���κ����֣�", "���±�", JOptionPane.WARNING_MESSAGE);
		}
		Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(myarea.getSelectedText());
		clipBoard.setContents(stringSelection, null);
	}
	
	
	//ճ��
	private void paste() throws UnsupportedFlavorException, IOException {
		String content_copy = "";
		// ����ϵͳ���а�
		Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

		// ��ȡ���а�����
		Transferable content = clipBoard.getContents(null);

		if (content != null) {
			// ����Ƿ����ı�����
			if (content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				content_copy = (String) content.getTransferData(DataFlavor.stringFlavor);

				// �ж��ı�������������ѡ��
				if (myarea.getSelectedText() != null) {
					myarea.replaceSelection(content_copy);
				} else {
					myarea.insert(content_copy, myarea.getSelectionStart());
				}
			}
		} 
	}

	
	
	//ɾ��
	private void delete() {

		if (myarea.getSelectedText() == null) {
			JOptionPane.showMessageDialog(null, "��û��ѡ���κ����֣�", "���±�", JOptionPane.WARNING_MESSAGE);
		}
		myarea.replaceSelection("");
	}
	public static void main(String[] args) {
		new Notepad().setVisible(true);
	}
}