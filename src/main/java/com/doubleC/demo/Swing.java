package com.doubleC.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class Swing extends JFrame{
    private JTextArea maxAllocation;
    private JTextArea allocInput;
    private JTextField availInput;
    private JTextArea outputAllocate;
    private JButton runButton;

    public Swing() {
        setTitle("银行家算法模拟");
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //输入区配置
        JPanel inputPanel = new JPanel(new GridLayout(3,2));

        //创建输入窗口
        maxAllocation = new JTextArea(8,20);
        allocInput = new JTextArea(8,20);
        availInput = new JTextField();

        inputPanel.add(new JLabel("最大需求 MAX(每行一个进程,空格分隔)"));
        inputPanel.add(new JScrollPane(maxAllocation));
        inputPanel.add(new JLabel("已分配 Allocation"));
        inputPanel.add(new JScrollPane(allocInput));
        inputPanel.add(new JLabel("系统可用资源 Available"));
        inputPanel.add(availInput);

        add(inputPanel, BorderLayout.NORTH);

        //输出区域
        outputAllocate = new JTextArea();
        outputAllocate.setEditable(false);
        add(new JScrollPane(outputAllocate),BorderLayout.CENTER);

        runButton = new JButton("运行算法");
        add(runButton,BorderLayout.SOUTH);

        runButton.addActionListener(e -> runBanker());


    }

    private void runBanker(){
        try {
            List<Process> processes = new ArrayList<>();

            //从JTA获取文本并处理成行
            String[] maxLines = maxAllocation.getText().trim().split("\n");
            String[] inputLines = allocInput.getText().trim().split("\n");
            String[] availLines = availInput.getText().trim().split("\\s+");

            int[] available = Arrays.stream(availLines).mapToInt(Integer::parseInt).toArray();

            for (int i = 0 ;i < maxLines.length; i++){
                int[] max = Arrays.stream(maxLines[i].trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
                int[] alloc = Arrays.stream(inputLines[i].trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();

                processes.add(new Process("P" + i,max,alloc));
            }

            Banker banker = new Banker(available,processes);
            List<Process> safeSeq = banker.isSafe();

            if (safeSeq == null){
                outputAllocate.setText("系统不安全!\n可能存在死锁");
            }
            else {
                outputAllocate.setText("系统是安全的!\n安全序列为 : \n");
                for (Process process: safeSeq){
                    outputAllocate.append(process.getpID() + " ");
                }
            }

        } catch (Exception e) {
            outputAllocate.setText(e.getMessage());
        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Swing().setVisible(true));
    }
}
