package nodeTest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Random;
/**
 * Created by hc2twv on 22/2/17.
 */
public class NodeGUI{
    private int id;
    private String MAC;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField txtTmpLow;
    private JTextField txtTmpHigh;
    private JTextArea txtInfo;
    private JTextField txtDioLow;
    private JTextField txtDioHigh;
    private JButton updBtnTmp;
    private JButton updBtnDio;
    private JTextField txtMonoL;
    private JTextField txtMonoH;
    private JButton updCO;
    private JTextField txtVllL;
    private JTextField txtVLLH;
    private JButton updVLL;
    private JButton updPLG;
    private JTextField txtPLGL;
    private JTextField txtPLGH;
    private JButton alarm1;
    private JButton stopAlarmButton;

    //Variables
    private double CO2Value= 0, COValue = 0, TMPValue= 0;
    private int alarm = 0, emp= 0, emp1= 0;
    Random r = new Random();
    private Date inicialDate,finalDate;

    //Sensors
    Sensor tmp = new Sensor(1,20,25,"T");
    Sensor Co2 = new Sensor(2,300,330,"X");
    Sensor PLG = new Sensor(3,135,205,"P");
    Sensor VLL = new Sensor(4,0,5,"V");
    Sensor CO = new Sensor(5,20,26,"C");

    public NodeGUI(int _id, String _MAC) {
        this.id = _id;
        this.MAC = _MAC;
        createUIComponents(this.id);

        updBtnTmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tmp.setUmbral_inf(Double.parseDouble(txtTmpLow.getText()));
                tmp.setUmbral_sup(Double.parseDouble(txtTmpHigh.getText()));
                txtInfo.append("here\n");
            }
        });
        updBtnDio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Co2.setUmbral_inf(Double.parseDouble(txtDioLow.getText()));
                Co2.setUmbral_sup(Double.parseDouble(txtDioHigh.getText()));
            }
        });
        updCO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CO.setUmbral_inf(Double.parseDouble(txtMonoL.getText()));
                CO.setUmbral_sup(Double.parseDouble(txtMonoH.getText()));
            }
        });
        updPLG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PLG.setUmbral_inf(Double.parseDouble(txtPLGL.getText()));
                PLG.setUmbral_sup(Double.parseDouble(txtPLGH.getText()));
            }
        });
        updVLL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VLL.setUmbral_inf(Double.parseDouble(txtVllL.getText()));
                VLL.setUmbral_sup(Double.parseDouble(txtVLLH.getText()));
            }
        });
        alarm1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alarm = 1;

            }
        });
        stopAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alarm = 0;
                emp = 0;
            }
        });
        getData();
    }

    private void createUIComponents(int id) {
        JFrame frame = new JFrame("Node: "+id);
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Temperature
        txtTmpLow.setText(tmp.getUmbral_inf().toString());
        txtTmpHigh.setText(tmp.getUmbral_sup().toString());

        //Co2
        txtDioLow.setText(Co2.getUmbral_inf().toString());
        txtDioHigh.setText(Co2.getUmbral_sup().toString());

        //CO
        txtMonoL.setText(CO.getUmbral_inf().toString());
        txtMonoH.setText(CO.getUmbral_sup().toString());

        //PLG
        txtPLGL.setText(PLG.getUmbral_inf().toString());
        txtPLGH.setText(PLG.getUmbral_sup().toString());

        //VLL
        txtVllL.setText(VLL.getUmbral_inf().toString());
        txtVLLH.setText(VLL.getUmbral_sup().toString());

        JScrollPane scroll = new JScrollPane (txtInfo,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scroll);
        frame.pack();
        frame.setVisible(true);
    }

    private void getData(){
        DatagramSocket dsocket = null;
        DatagramPacket packet = null;
        try{
            String host = "127.0.0.1";
            int port = 25555;
            // Get the internet address of the specified host
            InetAddress address = InetAddress.getByName(host);
            byte[] message = null;
            dsocket = new DatagramSocket();
            while(true){
                if(alarm == 0) {
                    TMPValue = tmp.getValue();
                    CO2Value = Co2.getValue();
                    COValue = CO.getValue();
                }else{
                    if(emp == 0){
                        String msj = "iniciar:0:0:0:0:0";
                        message = msj.getBytes();
                        packet = new DatagramPacket(message, message.length,address, port);
                        dsocket.send(packet);
                        emp = 1;
                    }
                    TMPValue = getDataAlarmTmp(TMPValue);
                    CO2Value = getDataAlarmCo2(CO2Value);
                    COValue = getDataAlarmCo(COValue);
                }
                String msj = this.id+":"+tmp.getId()+":"+this.MAC+":"+tmp.getType()+":"+TMPValue+":CHK";
                message = msj.getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                txtInfo.append("TMP: "+msj);
                txtInfo.append("\n");

                msj = this.id+":"+Co2.getId()+":"+this.MAC+":"+Co2.getType()+":"+CO2Value+":CHK";
                message = msj.getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                txtInfo.append("CO2: "+msj);
                txtInfo.append("\n");

                msj = this.id+":"+CO.getId()+":"+this.MAC+":"+CO.getType()+":"+COValue+":CHK";
                message = msj.getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                txtInfo.append("CO: "+msj);
                txtInfo.append("\n");
                msj = this.id+":"+PLG.getId()+":"+this.MAC+":"+PLG.getType()+":"+PLG.getValue()+":CHK";
                message = msj.getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                txtInfo.append("PLG: "+msj);
                txtInfo.append("\n");
                msj = this.id+":"+VLL.getId()+":"+this.MAC+":"+VLL.getType()+":"+VLL.getValue()+":CHK";
                message = msj.getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                txtInfo.append("VLL: "+msj);
                txtInfo.append("\n");
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally{
            dsocket.close();
        }
    }


    double getDataAlarmTmp(double value){
        double inc = r.nextDouble()*(20) + 1;
        return value + (inc/10);
    }
    double getDataAlarmCo2(double value){
        double inc = r.nextDouble()*(15) + 5;
        return value + (inc);
    }
    double getDataAlarmCo(double value){
        double inc = r.nextDouble()*(15) + 5;
        return value + (inc);
    }


}
