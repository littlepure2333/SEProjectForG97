package views;

import bin.ScooterManage;
import bin.AppState;
import bin.StationManage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReturnPanel extends JPanel implements PanelStateMonitor {
    private static JPanel[] slotPanel;
    private static JLabel myLabel = new JLabel();
    private static JLabel selectLabel = new JLabel();
    private JPanel submitPanel = new SubmitPanel();
    private JPanel subPanel;

    ReturnPanel() {
        JPanel upperPanel = new UpperPanel();
        slotPanel = new JPanel[8];
        for(int i=0;i<=7;i++)
            slotPanel[i] = new EmptySlotPanel();
        subPanel = new SubPanel();

        this.setLayout(new GridLayout(3,1));
        this.add(upperPanel);
        this.add(subPanel);
        this.add(submitPanel);

        this.setVisible(true);
    }


    @Override
    public void stateChanged() {

    }
    /**
     * 界面后台信息的改动
     * 直接影响界面内槽位的显示
     * 站点必须在之前已经指定好，本类不要调用该方法
     * 更新文本信息
     * 适用于初次/再次进入该界面
     */
    @Override
    public void update() {
        /*
		从后台读取slot数据并设置图片
		 */
        for (int i=0;i<=7;i++) {
            if (AppState.getCurrentStation().slot[i] != null)
                slotPanel[i] = new OccupiedSlotPanel();
            else slotPanel[i] = new EmptySlotPanel();
        }
        subPanel.removeAll();
        for (int i=0;i<=7;i++)
            subPanel.add(slotPanel[i]);

        /*
		预判断slot整体情况
		 */
        if (!checkIsFull()) {
            myLabel.setText("Ready for return your scooter......\r\n");
            selectLabel.setText("Please use the one with flashing......");
            submitPanel.setVisible(true);
        }
        else {
            myLabel.setText("No available slot in this station!\r\n");
            selectLabel.setText("Please check other station!");
            submitPanel.setVisible(false);
        }
    }

    /**
     * 检查站点是否为满
     * @return true-全满，false-有空位
     */
    private boolean checkIsFull() {
        for (int i=0;i<=7;i++) {
            if (AppState.getCurrentStation().slot[i] == null) {
                return false;
            }
        }
        return true;
    }

    class UpperPanel extends JPanel{
        UpperPanel() {
            myLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
            selectLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));

            this.setLayout(new GridLayout(3,1));
            this.add(myLabel);
            this.add(selectLabel);

        }
    }

    class OccupiedSlotPanel extends JPanel{
        public void paintComponent(Graphics g) {
            ImageIcon image =new ImageIcon("./media/scooter.jpg");
            image.setImage(image.getImage().getScaledInstance(this.getWidth(),this.getHeight(),
                    Image.SCALE_AREA_AVERAGING));
            g.drawImage(image.getImage(),0,0,this);
        }

    }
    class EmptySlotPanel extends JPanel{
        public void paintComponent(Graphics g) {
            ImageIcon image =new ImageIcon("./media/null.jpg");
            image.setImage(image.getImage().getScaledInstance(this.getWidth(),this.getHeight(),
                    Image.SCALE_AREA_AVERAGING));
            g.drawImage(image.getImage(),0,0,this);
        }

    }

    class SubPanel extends JPanel{
        SubPanel() {
            this.setLayout(new GridLayout(1,8));

            for (int i=0;i<=7;i++)
                this.add(slotPanel[i]);
        }
    }

    static class SubmitPanel extends JPanel implements ActionListener {
        private static JButton helpButton;
        int site;
        SubmitPanel() {
            helpButton=new JButton("Help me pick a empty slot");
            this.setLayout(new GridLayout(2,1));
            helpButton.setFont(new Font("Times New Roman", Font.PLAIN, 40));
            this.add(new JLabel(""));
            this.add(helpButton);
            helpButton.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e){
            Thread thread = new Thread(new WaitForReturn());
            String actionCommand = e.getActionCommand();
            /*
			提示阶段
			 */
            if(actionCommand.equals("Help me pick a empty slot")) {
                helpButton.setText("Return");
                //从左到右找到一个空车位
                for(;site<=7;site++) {
                    if(AppState.getCurrentStation().slot[site] == null) {
                        StationManage.chooseFlashSlot(site);
                        break;
                    }
                }
                thread.start();
//                /*
//                闪光操作（还车）
//                 */
//                for(int j=0;j<20;j++) {
//                    if(j%2==0) {
//                        setSlotViewEmptyFlash();
//                        long y= 0x10008000L;
//                        for(;y>=0;y--) {
//                        }
//                    }
//                    else {
//                        setSlotViewEmpty();
//                        long y= 0x10008000L;
//                        for(;y>=0;y--) {
//                        }
//                    }
//                }
            }
            /*
			放车阶段
			 */
            if(actionCommand.equals("Return")) {
//                ScooterManage.returnScooter();
                WaitForReturn.abort();
                myLabel.setText("You have returned your scooter!\r\n");
                selectLabel.setText("Thank you for using!");
                helpButton.setText("Click here to log out");
                setSlotViewOccupied();
            }
            /*
			完成阶段
			 */
            if(actionCommand.equals("Click here to log out")) {
                Windows.backToMenu();
            }
        }

        /*
        闪光线程
         */
        static class WaitForReturn implements Runnable {
            private static int i;
            @Override
            public void run() {
                for (i = 0; i < 20; i++) {
                    setSlotViewEmpty();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setSlotViewEmptyFlash();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(i == 21) {
                    myLabel.setText("Time expired\r\n");
                    selectLabel.setText("Please return to previous page");
                    helpButton.setText("Time expired");
                }
            }

            static void abort() {
                i = 22;
                setSlotViewOccupied();
            }
        }

        /**
         * 将目标槽位的图片修改为：null-无灯
         */
        private static void setSlotViewEmpty() {
            JPanel slot = slotPanel[AppState.getCurrentSlot()];
            ImageIcon image = new ImageIcon("./media/null.jpg");
            image.setImage(image.getImage().getScaledInstance(slot.getWidth(), slot.getHeight(), Image.SCALE_AREA_AVERAGING));
            slot.getGraphics().drawImage(image.getImage(),0,0,slot);
        }

        /**
         * 将目标槽位的图片修改为：有车-无灯
         */
        private static void setSlotViewOccupied() {
            JPanel slot = slotPanel[AppState.getCurrentSlot()];
            ImageIcon image = new ImageIcon("./media/scooter.jpg");
            image.setImage(image.getImage().getScaledInstance(slot.getWidth(), slot.getHeight(), Image.SCALE_AREA_AVERAGING));
            slot.getGraphics().drawImage(image.getImage(),0,0,slot);
        }

        /**
         * 将目标槽位的图片修改为：无车-有灯
         */
        private static void setSlotViewEmptyFlash() {
            JPanel slot = slotPanel[AppState.getCurrentSlot()];
            ImageIcon image = new ImageIcon("./media/nullflash.jpg");
            image.setImage(image.getImage().getScaledInstance(slot.getWidth(), slot.getHeight(), Image.SCALE_AREA_AVERAGING));
            slot.getGraphics().drawImage(image.getImage(),0,0,slot);
        }
    }

}