package com.todolist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;


public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JTextField deadline;
	public JDateChooser deadlineChooser;
	public JTextField task;
	public JPanel taskListPanel;

	
	public View() {
		
		//画面を作成
		setTitle("TODOリスト");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//期限日をカレンダー形式で入力
        deadlineChooser = new JDateChooser();
        deadlineChooser.setDateFormatString("yyyy-MM-dd"); 
        deadlineChooser.setPreferredSize(new Dimension(150, 25));
        contentPane.add(deadlineChooser);
		
        //タスクを入力
		task = new JTextField("タスクを入力", 15);
		contentPane.add(task);
		setPlaceholder(task,"タスクを入力");
		
		//登録ボタンを作成
		JButton addButton = new JButton("登録");
		contentPane.add(addButton);
		
		//登録ボタン押下時の処理はコントローラに委譲
		addButton.addActionListener(new TaskManager(this));
		
		//タスク表示エリア
	    taskListPanel = new JPanel();
	    taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
	    JScrollPane scrollPane = new JScrollPane(taskListPanel);
	    scrollPane.setPreferredSize(new Dimension(400, 150));
	    contentPane.add(scrollPane);
	}
	
	public void addTaskPanel(String taskText, String deadlineText, TaskManager manager) {
	    JPanel row = new JPanel();
	    row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
	    row.setAlignmentX(Component.LEFT_ALIGNMENT); 
	    
	    JLabel label = new JLabel("期限：" + deadlineText + "　タスク：" + taskText);
	    JButton deleteButton = new JButton("削除");
	    int height = label.getPreferredSize().height;
	    deleteButton.setMaximumSize(new Dimension(80, height));

		//削除ボタン押下時の処理はコントローラに委譲
	    deleteButton.addActionListener(e -> manager.deleteTask(row, taskText, deadlineText));
	    
	    row.add(label);
	    row.add(Box.createHorizontalStrut(10));  // ラベルとボタンの間隔
	    row.add(deleteButton);
	    row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	    taskListPanel.add(row);
	    taskListPanel.revalidate();
	    taskListPanel.repaint();
	}

	
	//期限日を文字列で取得
	public String getSelectedDeadline() {
        Date date = deadlineChooser.getDate();
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
        
	//テキストボックスをクリックするとプレースホルダーを消す設定
	private void setPlaceholder(JTextField field, String placeholder) {
		field.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}
			public void focusLost(FocusEvent e) {
				if (field.getText().isEmpty()) {
					field.setForeground(Color.GRAY);
					field.setText(placeholder);
				}
			}
		});
	}
}

