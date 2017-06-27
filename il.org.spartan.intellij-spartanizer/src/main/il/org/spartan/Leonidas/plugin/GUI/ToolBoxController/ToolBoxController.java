package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;


import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.plugin.GUI.LeonidasIcon;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Amir Sagiv
 * @since 24/04/2017
 */
class ToolBoxController extends JFrame {
    private static boolean active = false;
    private JPanel mainPanel;
    private JButton applyButton;
    private JButton clearAllButton;
    private JButton selectAllButton;
    private JSplitPane splitPanel;
    private JPanel rightPanel;
    private JPanel infoPanel;
    private JPanel buttonsPanel;
    private JScrollPane tippersPane;
    private JTextArea textArea1;
    private JButton OKButton;

    private CheckBoxList list;

    public ToolBoxController() {
        super("Spartanizer ToolBox Controller");
        if(active){return;}
        active = true;
        LeonidasIcon.apply(this);
        list = new CheckBoxList();
        List<Tipper> tipsList = Toolbox.getInstance().getCurrentTippers();
        List<Tipper> allTipsList = Toolbox.getInstance().getAllTippers();
        allTipsList.forEach(tip -> {
            if (tipsList.contains(tip))
                list.addCheckbox(new JCheckBox(tip.name(), true));
            else
                list.addCheckbox(new JCheckBox(tip.name(), false));
        });

        list.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());

                if (index > -1 && index < list.getNumOfElements()) {
                    JCheckBox checkbox = (JCheckBox)
                            list.getModel().getElementAt(index);
                    Map<String, String> examples = null;
                    LeonidasTipperDefinition ltd = Toolbox.getInstance().getTipperInstanceByName(checkbox.getText());
                    if (ltd == null) {
                        Tipper tipper = Toolbox.getInstance().getTipperByName(checkbox.getText());
                        examples = tipper.getExamples();
                    } else { // leonidasTipper
                        examples = ltd.getExamples();
                    }
                    String before = "";
                    String after = "";
                    for (Map.Entry<String, String> entry : examples.entrySet()) {
                        before = entry.getKey();
                        after = entry.getValue();
                        if (before == null || after == null) {
                            continue;
                        }
                        break;
                    }
                    String text = "Before:\n" + before + "\n\n" +
                            "After:\n" + after;
                    textArea1.setText(text);
                }
            }
        });

        OKButton.addActionListener(e ->{
            applyListener();
            this.dispose();
        });
        textArea1.setEditable(false);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                applyButton.setEnabled(true);
            }
        });
        tippersPane.setViewportView(list);
        selectAllButton.addActionListener(e -> selectAllListener());
        clearAllButton.addActionListener(e -> clearAllListener());
        applyButton.addActionListener(e -> applyListener());

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                active = false;
            }
        });

        setContentPane(mainPanel);
        setPreferredSize(new Dimension(1200, 600));
        setResizable(false);
        pack();
        setVisible(true);
    }

    private void applyListener() {
        applyButton.setEnabled(false);
        List<String> updateList = new ArrayList<>();
        for (int i = 0; i < list.getNumOfElements(); i++) {
            JCheckBox checkbox = (JCheckBox)
                    list.getModel().getElementAt(i);
            if (checkbox.isSelected()) {
                updateList.add(checkbox.getText());
            }
        }
        Toolbox.getInstance().updateTipperList(updateList);
        Project p = Utils.getProject();
        DaemonCodeAnalyzer.getInstance(p).restart();

    }

    private void clearAllListener() {
        list.setAllCheckBoxes(false);
    }

    private void selectAllListener() {
        list.setAllCheckBoxes(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        splitPanel = new JSplitPane();
        splitPanel.setDividerLocation(400);
        splitPanel.setDividerSize(10);
        splitPanel.setEnabled(false);
        mainPanel.add(splitPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPanel.setRightComponent(rightPanel);
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rightPanel.add(infoPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Choose the tippers you want to be notified on:");
        infoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textArea1 = new JTextArea();
        infoPanel.add(textArea1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 10, 20, 10), -1, -1));
        buttonsPanel.setEnabled(true);
        rightPanel.add(buttonsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        applyButton = new JButton();
        applyButton.setText("Apply");
        buttonsPanel.add(applyButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        buttonsPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        clearAllButton = new JButton();
        clearAllButton.setText("Clear All");
        buttonsPanel.add(clearAllButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectAllButton = new JButton();
        selectAllButton.setText("Select All");
        buttonsPanel.add(selectAllButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tippersPane = new JScrollPane();
        tippersPane.setVerticalScrollBarPolicy(22);
        splitPanel.setLeftComponent(tippersPane);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}


