package ui.gui;

import config.Config;
import config.WorkMode;
import hanyuu.ext.ScriptContainer;
import hanyuu.ext.interfaces.OCR;
import hanyuu.managers.ThreadManager;
import hanyuu.net.proxy.HttpProxy;
import hanyuu.net.wipe.AbstractWipe;
import ui.LookAndFeel;
import ui.interfaces.UI;
import ui.other.Faggot;
import utils.CaptchaUtils;
import utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public final class GUI extends JFrame implements UI {
    private JFileChooser chooser = new JFileChooser();

    private final ThreadManager tm;

    private DefaultListModel pdlm;

    private static final Color green = new Color(0, 153, 51);

    private static final Cursor handCursor = Cursor.getPredefinedCursor(12);

    private static final Cursor simpleCursor = Cursor.getPredefinedCursor(0);

    private boolean re = false;

    private Document logDocument;

    private ArrayList<AbstractWipe> wt = new ArrayList<>();

    private ImageIcon captchaImage;

    private AbstractWipe selectedWipe;

    private boolean fromTray;

    private DefaultTableModel dtm;

    private DefaultListModel sdlm;

    private JButton AddRrow;

    private JTextField FontName;

    private JButton RemoveRrow;

    private JLabel all;

    private JTextField board;

    private JComboBox captchas;

    private JComboBox chans;

    private JButton check;

    private JButton checkAll;

    private JCheckBox checkContent;

    private JCheckBox checkOnLoad;

    private JCheckBox checkOnStart;

    private JLabel cimg;

    private JSpinner colorRnd;

    private JCheckBox colorRndRnd;

    private JTextField ctxt;

    private JButton delSelected;

    private JButton delete;

    private JButton dellAll;

    private JSpinner deltaY;

    private JCheckBox dontEdit;

    private JCheckBox dontEditPixels;

    private JCheckBox dontJamp;

    private JCheckBox dontTrayMsg;

    private JCheckBox dontUseScript;

    private JTextField email;

    private JRadioButton empty;

    private JLabel failed;

    private JSpinner fontSize;

    private JButton formerCaptcha;

    private JButton fromfile;

    private JLabel hanyuu;

    private JButton jButton1;

    private JLabel jLabel1;

    private JLabel jLabel10;

    private JLabel jLabel11;

    private JLabel jLabel12;

    private JLabel jLabel13;

    private JLabel jLabel14;

    private JLabel jLabel15;

    private JLabel jLabel16;

    private JLabel jLabel17;

    private JLabel jLabel18;

    private JLabel jLabel19;

    private JLabel jLabel2;

    private JLabel jLabel20;

    private JLabel jLabel21;

    private JLabel jLabel22;

    private JLabel jLabel23;

    private JLabel jLabel24;

    private JLabel jLabel25;

    private JLabel jLabel26;

    private JLabel jLabel27;

    private JLabel jLabel28;

    private JLabel jLabel29;

    private JLabel jLabel3;

    private JLabel jLabel30;

    private JLabel jLabel31;

    private JLabel jLabel32;

    private JLabel jLabel33;

    private JLabel jLabel34;

    private JLabel jLabel35;

    private JLabel jLabel36;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel jLabel6;

    private JLabel jLabel7;

    private JLabel jLabel8;

    private JLabel jLabel9;

    private JPanel jPanel1;

    private JPanel jPanel2;

    private JScrollPane jScrollPane1;

    private JScrollPane jScrollPane2;

    private JScrollPane jScrollPane4;

    private JScrollPane jScrollPane5;

    private JTextField key;

    private JButton load;

    private JButton loadScript;

    private JTextPane log;

    private JScrollPane logScroll;

    private JPanel main;

    private JTabbedPane mainPane;

    private JSpinner maxFileSize;

    private JTextPane message;

    private JComboBox modes;

    private JCheckBox msgCount;

    private JSpinner msgCountInteger;

    private JRadioButton myseparator;

    private JTextField name;

    private JButton nextCaptcha;

    private JCheckBox noFile;

    private JCheckBox noproxy;

    private JLabel ocrName;

    private JComboBox ocrs;

    private JCheckBox one;

    private JCheckBox ontop;

    private JSpinner page;

    private JTextField password;

    private JPanel paste;

    private JCheckBox pasteNoMsg;

    private JCheckBox pasteOnPic;

    private JTextField path;

    private JPanel pics;

    private JLabel picsCount;

    private JCheckBox picsPuck;

    private JSpinner picsTxtB;

    private JSpinner picsTxtG;

    private JSpinner picsTxtR;

    private JSpinner pixelEdit;

    private JPanel proxy;

    private JProgressBar proxyProgress;

    private JList proxys;

    private JCheckBox randomBytes;

    private JCheckBox randomPicGenerate;

    private JCheckBox randomize;

    private JButton reAll;

    private JButton reLoad;

    private JButton reMe;

    private JButton reload;

    private JTable replacements;

    private JCheckBox reverseCaptcha;

    private JSpinner rndCount;

    private JLabel runThreads;

    private JButton saver;

    private JTextField scaleH;

    private JTextField scaleW;

    private JLabel scriptInfo;

    private JPanel scripts;

    private JList scriptsList;

    private JButton selectFileOrFolder;

    private JButton send;

    private JPanel settings;

    private JButton shutdown;

    private JCheckBox silentBump;

    private JComboBox smartErrorAction;

    private JTextField smartErrorCount;

    private JCheckBox smartErrorHandler;

    private JButton start;

    private JSpinner startX;

    private JSpinner startY;

    private JLabel status;

    private JButton stopSelected;

    private JLabel suc;

    private JTextField symbol;

    private JTextField syte;

    private JTextField syteKey;

    private JTextField theme;

    private JTextField thread;

    private JSpinner threads;

    private JSpinner timeout;

    private JCheckBox useParser;

    private JCheckBox useTmp;

    private JTextField userAgent;

    private JCheckBox waitForNotReady;

    public enum type {
        Info((String) GUI.green),
        Error((String) Color.RED);

        private final Color kolor;

        type(Color c) {
            this.kolor = c;
        }

        public SimpleAttributeSet getAtributes() {
            SimpleAttributeSet h = new SimpleAttributeSet();
            h.addAttribute(StyleConstants.Foreground, this.kolor);
            return h;
        }
    }

    public GUI(ThreadManager tm) {
        LookAndFeel.initLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        this.tm = tm;
        initComponents();
        this.logDocument = this.log.getDocument();
        this.log.setEditable(false);
        this.log.setBackground(Color.BLACK);
        this.log.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setBounds(x, y, getWidth(), getHeight());
        this.pdlm = new DefaultListModel();
        this.sdlm = new DefaultListModel();
        this.proxys.setModel(this.pdlm);
        this.scriptsList.setModel(this.sdlm);
        this.suc.setForeground(green);
        this.failed.setForeground(Color.RED);
        this.timeout.setToolTipText("Таймаут в милисекундах.");
        this.hanyuu.setToolTipText("Молись Богине-будь няшкой");
        setUpState(false);
        this.delSelected.setEnabled(false);
        this.stopSelected.setEnabled(false);
        if (tm.getChanManager().size() == 0) {
            JOptionPane.showMessageDialog(this, "Нету чанов не могу работать\nХау~", "Ханю", 0);
            System.exit(0);
        }
        myInit();
    }

    private void myInit() {
        this.dtm = (DefaultTableModel) this.replacements.getModel();
        switch (Config.workMode) {
            case WipeBoard:
                this.page.setEnabled(false);
                this.thread.setEnabled(false);
                this.modes.setSelectedIndex(0);
                break;
            case Force:
                this.page.setEnabled(true);
                this.thread.setEnabled(false);
                this.modes.setSelectedIndex(1);
                break;
            case OnZeroPage:
                this.thread.setEnabled(true);
                this.page.setEnabled(false);
                this.modes.setSelectedIndex(2);
                break;
            case WipeThread:
                this.thread.setEnabled(true);
                this.page.setEnabled(false);
                this.modes.setSelectedIndex(3);
                break;
        }
        for (String[] s : Config.replacements)
            this.dtm.addRow((Object[]) s);
        this.randomPicGenerate.setSelected(Config.randomPicGenerate);
        this.randomize.setSelected(Config.randomizePaste);
        this.useTmp.setSelected(Config.useTmp);
        this.colorRndRnd.setSelected(Config.colorRndRnd);
        this.colorRnd.setEnabled(!Config.colorRndRnd);
        this.checkOnLoad.setSelected(Config.checkOnLoad);
        this.selectFileOrFolder.setEnabled(!Config.BokuNoFile);
        this.path.setEnabled(!Config.BokuNoFile);
        this.noFile.setSelected(Config.BokuNoFile);
        this.syte.setEnabled(Config.contentCheck);
        this.syteKey.setEnabled(Config.contentCheck);
        this.page.setValue(Integer.valueOf(Config.page));
        this.dontEditPixels.setSelected(Config.dontEditPixels);
        this.checkContent.setSelected(Config.contentCheck);
        this.silentBump.setSelected(Config.silentBump);
        this.userAgent.setText(Config.userAgent);
        this.pixelEdit.setValue(Integer.valueOf(Config.pixelEdit));
        this.checkOnStart.setSelected(Config.emulateWipe);
        this.syte.setText(Config.syte);
        this.syteKey.setText(Config.syteKey);
        if (this.tm.getImgManager().isPathDir())
            this.picsCount.setText("Всего пикчей в паке: " + String.valueOf(this.tm.getImgManager().filesCountInDir()));
        this.password.setText(Config.password);
        this.path.setText(Config.path);
        this.rndCount.setValue(Integer.valueOf(Config.rndCount));
        this.rndCount.setEnabled(Config.randomizePaste);
        this.one.setSelected(Config.onePic);
        this.noproxy.setSelected(Config.noProxy);
        setProxyState(Boolean.valueOf(!Config.noProxy));
        this.thread.setText(Config.thread);
        this.timeout.setValue(Integer.valueOf(Config.timeout));
        this.message.setText(Config.msg);
        this.theme.setText(Config.theme);
        this.board.setText(Config.board);
        this.useParser.setSelected(Config.parser);
        this.name.setText(Config.name);
        this.pasteOnPic.setSelected(Config.pasteOnPic);
        this.pasteNoMsg.setSelected(Config.pasteNoMsg);
        this.pasteNoMsg.setEnabled(Config.pasteOnPic);
        this.pasteNoMsg.setEnabled(Config.pasteOnPic);
        this.picsTxtR.setEnabled(Config.pasteOnPic);
        this.picsTxtG.setEnabled(Config.pasteOnPic);
        this.picsTxtB.setEnabled(Config.pasteOnPic);
        this.dontEditPixels.setEnabled(Config.dontEditPixels);
        this.threads.setValue(Integer.valueOf(Config.threads));
        this.colorRnd.setValue(Integer.valueOf(Config.colorRnd));
        setPasteCount(this.tm.getCopyPasteManager().size());
        this.empty.setSelected(Config.emptySeparator);
        this.myseparator.setSelected(!Config.emptySeparator);
        this.symbol.setEnabled(!Config.emptySeparator);
        this.symbol.setText(Config.separator);
        this.randomize.setSelected(Config.randomizePaste);
        this.msgCount.setSelected(Config.msgCount);
        this.msgCountInteger.setValue(Integer.valueOf(Config.msgCountInt));
        this.msgCountInteger.setEnabled(Config.msgCount);
        this.picsPuck.setSelected(Config.picsPuck);
        this.dontEdit.setSelected(Config.picsNotEdit);
        this.maxFileSize.setValue(Integer.valueOf(Config.maxFileSize));
        this.dontEdit.setEnabled(!Config.BokuNoFile);
        this.picsPuck.setEnabled(!Config.BokuNoFile);
        this.picsCount.setEnabled(!Config.BokuNoFile);
        this.maxFileSize.setEnabled(!Config.BokuNoFile);
        setPicsEnable((!Config.BokuNoFile && !Config.picsNotEdit));
        this.picsTxtR.setValue(Integer.valueOf(Config.picsTxtR));
        this.picsTxtG.setValue(Integer.valueOf(Config.picsTxtG));
        this.picsTxtB.setValue(Integer.valueOf(Config.picsTxtB));
        this.startX.setValue(Integer.valueOf(Config.startX));
        this.startY.setValue(Integer.valueOf(Config.startY));
        this.deltaY.setValue(Integer.valueOf(Config.deltaY));
        this.FontName.setText(Config.FontName);
        this.fontSize.setValue(Integer.valueOf(Config.fontSize));
        this.randomBytes.setSelected(Config.randomBytes);
        this.randomBytes.setEnabled(Config.useTmp);
        this.dontTrayMsg.setSelected(Config.dontTrayMsg);
        this.email.setText(Config.email);
        this.key.setText(Config.keyForServices);
        this.scaleW.setText(String.valueOf(Config.scaleW));
        this.scaleH.setText(String.valueOf(Config.scaleH));
        this.silentBump.setEnabled(Config.parser);
        this.reverseCaptcha.setSelected(Config.reverseCaptcha);
        this.smartErrorHandler.setSelected(Config.smartErrorHandler);
        this.smartErrorCount.setEnabled(Config.smartErrorHandler);
        this.smartErrorCount.setText(String.valueOf(Config.smartErrorCount));
        this.smartErrorAction.setEnabled(Config.smartErrorHandler);
        this.smartErrorAction.setSelectedIndex(Config.smartErrorAction);
        this.waitForNotReady.setSelected(Config.waitForNotReady);
        this.message.setText(Config.msg);
    }

    private void initComponents() {
        this.jPanel2 = new JPanel();
        this.jLabel6 = new JLabel();
        this.jPanel1 = new JPanel();
        this.jLabel31 = new JLabel();
        this.mainPane = new JTabbedPane();
        this.main = new JPanel();
        this.start = new JButton();
        this.hanyuu = new JLabel();
        this.captchas = new JComboBox();
        this.cimg = new JLabel();
        this.ctxt = new JTextField();
        this.reMe = new JButton();
        this.send = new JButton();
        this.reAll = new JButton();
        this.status = new JLabel();
        this.suc = new JLabel();
        this.failed = new JLabel();
        this.ontop = new JCheckBox();
        this.runThreads = new JLabel();
        this.nextCaptcha = new JButton();
        this.formerCaptcha = new JButton();
        this.delSelected = new JButton();
        this.stopSelected = new JButton();
        this.dellAll = new JButton();
        this.logScroll = new JScrollPane();
        this.log = new JTextPane();
        this.dontJamp = new JCheckBox();
        this.jButton1 = new JButton();
        this.settings = new JPanel();
        this.chans = new JComboBox();
        this.jLabel5 = new JLabel();
        this.page = new JSpinner();
        this.jLabel7 = new JLabel();
        this.board = new JTextField();
        this.jLabel13 = new JLabel();
        this.jLabel8 = new JLabel();
        this.timeout = new JSpinner();
        this.thread = new JTextField();
        this.jLabel9 = new JLabel();
        this.jLabel12 = new JLabel();
        this.threads = new JSpinner();
        this.silentBump = new JCheckBox();
        this.msgCountInteger = new JSpinner();
        this.msgCount = new JCheckBox();
        this.jLabel15 = new JLabel();
        this.jLabel16 = new JLabel();
        this.userAgent = new JTextField();
        this.key = new JTextField();
        this.ocrName = new JLabel();
        this.ocrs = new JComboBox();
        this.modes = new JComboBox();
        this.useParser = new JCheckBox();
        this.jLabel4 = new JLabel();
        this.jLabel33 = new JLabel();
        this.jLabel34 = new JLabel();
        this.scaleW = new JTextField();
        this.scaleH = new JTextField();
        this.reverseCaptcha = new JCheckBox();
        this.dontTrayMsg = new JCheckBox();
        this.smartErrorHandler = new JCheckBox();
        this.smartErrorCount = new JTextField();
        this.smartErrorAction = new JComboBox();
        this.jLabel35 = new JLabel();
        this.waitForNotReady = new JCheckBox();
        this.proxy = new JPanel();
        this.jScrollPane1 = new JScrollPane();
        this.proxys = new JList();
        this.check = new JButton();
        this.checkAll = new JButton();
        this.delete = new JButton();
        this.checkOnLoad = new JCheckBox();
        this.checkContent = new JCheckBox();
        this.noproxy = new JCheckBox();
        this.load = new JButton();
        this.checkOnStart = new JCheckBox();
        this.syte = new JTextField();
        this.syteKey = new JTextField();
        this.jLabel1 = new JLabel();
        this.jLabel32 = new JLabel();
        this.proxyProgress = new JProgressBar();
        this.paste = new JPanel();
        this.all = new JLabel();
        this.reload = new JButton();
        this.empty = new JRadioButton();
        this.myseparator = new JRadioButton();
        this.symbol = new JTextField();
        this.randomize = new JCheckBox();
        this.fromfile = new JButton();
        this.jLabel14 = new JLabel();
        this.rndCount = new JSpinner();
        this.jLabel17 = new JLabel();
        this.jScrollPane4 = new JScrollPane();
        this.replacements = new JTable();
        this.AddRrow = new JButton();
        this.RemoveRrow = new JButton();
        this.saver = new JButton();
        this.theme = new JTextField();
        this.jLabel11 = new JLabel();
        this.jScrollPane2 = new JScrollPane();
        this.message = new JTextPane();
        this.name = new JTextField();
        this.jLabel10 = new JLabel();
        this.password = new JTextField();
        this.jLabel3 = new JLabel();
        this.email = new JTextField();
        this.jLabel36 = new JLabel();
        this.pics = new JPanel();
        this.path = new JTextField();
        this.selectFileOrFolder = new JButton();
        this.noFile = new JCheckBox();
        this.jLabel2 = new JLabel();
        this.picsPuck = new JCheckBox();
        this.dontEdit = new JCheckBox();
        this.picsCount = new JLabel();
        this.one = new JCheckBox();
        this.useTmp = new JCheckBox();
        this.maxFileSize = new JSpinner();
        this.jLabel18 = new JLabel();
        this.jLabel19 = new JLabel();
        this.colorRnd = new JSpinner();
        this.jLabel20 = new JLabel();
        this.pixelEdit = new JSpinner();
        this.pasteOnPic = new JCheckBox();
        this.dontEditPixels = new JCheckBox();
        this.pasteNoMsg = new JCheckBox();
        this.picsTxtR = new JSpinner();
        this.jLabel21 = new JLabel();
        this.jLabel22 = new JLabel();
        this.picsTxtG = new JSpinner();
        this.jLabel23 = new JLabel();
        this.picsTxtB = new JSpinner();
        this.jLabel24 = new JLabel();
        this.jLabel25 = new JLabel();
        this.jLabel26 = new JLabel();
        this.jLabel27 = new JLabel();
        this.startX = new JSpinner();
        this.startY = new JSpinner();
        this.colorRndRnd = new JCheckBox();
        this.jLabel28 = new JLabel();
        this.deltaY = new JSpinner();
        this.FontName = new JTextField();
        this.jLabel29 = new JLabel();
        this.fontSize = new JSpinner();
        this.jLabel30 = new JLabel();
        this.randomBytes = new JCheckBox();
        this.randomPicGenerate = new JCheckBox();
        this.scripts = new JPanel();
        this.jScrollPane5 = new JScrollPane();
        this.scriptsList = new JList();
        this.reLoad = new JButton();
        this.shutdown = new JButton();
        this.loadScript = new JButton();
        this.scriptInfo = new JLabel();
        this.dontUseScript = new JCheckBox();
        GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
        this.jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 100, 32767));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 100, 32767));
        this.jLabel6.setText("jLabel6");
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 100, 32767));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 100, 32767));
        this.jLabel31.setText("jLabel31");
        setDefaultCloseOperation(3);
        setTitle("Рогатулечка");
        setBackground(new Color(0, 0, 0));
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                GUI.this.formWindowClosing(evt);
            }
        });
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                GUI.this.formFocusGained(evt);
            }
        });
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent evt) {
                GUI.this.formWindowStateChanged(evt);
            }
        });
        this.main.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.mainMouseClicked(evt);
            }
        });
        this.main.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.mainFocusLost(evt);
            }
        });
        this.main.setLayout((LayoutManager) null);
        this.start.setText("Запустить");
        this.start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.startActionPerformed(evt);
            }
        });
        this.main.add(this.start);
        this.start.setBounds(440, 230, 121, 23);
        this.hanyuu.setIcon(new ImageIcon(getClass().getResource("/ui/res/h.png")));
        this.hanyuu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.hanyuuMouseClicked(evt);
            }
        });
        this.main.add(this.hanyuu);
        this.hanyuu.setBounds(0, 0, 100, 131);
        this.captchas.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                GUI.this.captchasItemStateChanged(evt);
            }
        });
        this.main.add(this.captchas);
        this.captchas.setBounds(131, 76, 190, 22);
        this.cimg.setMaximumSize(new Dimension(999, 999));
        this.cimg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.cimgMouseClicked(evt);
            }

            public void mouseEntered(MouseEvent evt) {
                GUI.this.cimgMouseEntered(evt);
            }
        });
        this.main.add(this.cimg);
        this.cimg.setBounds(327, 11, 267, 97);
        this.ctxt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                GUI.this.ctxtKeyPressed(evt);
            }
        });
        this.main.add(this.ctxt);
        this.ctxt.setBounds(327, 148, 270, 20);
        this.reMe.setText("Обновить");
        this.reMe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.reMeActionPerformed(evt);
            }
        });
        this.main.add(this.reMe);
        this.reMe.setBounds(125, 147, 196, 23);
        this.send.setText("Послать");
        this.send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.sendActionPerformed(evt);
            }
        });
        this.main.add(this.send);
        this.send.setBounds(410, 180, 102, 23);
        this.reAll.setText("Обновить все");
        this.reAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.reAllActionPerformed(evt);
            }
        });
        this.main.add(this.reAll);
        this.reAll.setBounds(125, 117, 196, 23);
        this.main.add(this.status);
        this.status.setBounds(330, 210, 260, 14);
        this.suc.setText("Всего успешно:   0");
        this.main.add(this.suc);
        this.suc.setBounds(200, 180, 119, 14);
        this.failed.setText("Всего неудачно: 0");
        this.main.add(this.failed);
        this.failed.setBounds(200, 200, 119, 14);
        this.ontop.setText("Поверх всех окон");
        this.ontop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.ontopActionPerformed(evt);
            }
        });
        this.main.add(this.ontop);
        this.ontop.setBounds(10, 180, 115, 23);
        this.main.add(this.runThreads);
        this.runThreads.setBounds(0, 260, 640, 30);
        this.nextCaptcha.setText(">");
        this.nextCaptcha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.nextCaptchaActionPerformed(evt);
            }
        });
        this.main.add(this.nextCaptcha);
        this.nextCaptcha.setBounds(550, 180, 43, 23);
        this.formerCaptcha.setText("<");
        this.formerCaptcha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.formerCaptchaActionPerformed(evt);
            }
        });
        this.main.add(this.formerCaptcha);
        this.formerCaptcha.setBounds(330, 180, 43, 23);
        this.delSelected.setText("Удалить");
        this.delSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.delSelectedActionPerformed(evt);
            }
        });
        this.main.add(this.delSelected);
        this.delSelected.setBounds(10, 230, 100, 23);
        this.stopSelected.setText("Остановить");
        this.stopSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.stopSelectedActionPerformed(evt);
            }
        });
        this.main.add(this.stopSelected);
        this.stopSelected.setBounds(120, 230, 111, 23);
        this.dellAll.setText("Удалить все посты");
        this.dellAll.setEnabled(false);
        this.dellAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.dellAllActionPerformed(evt);
            }
        });
        this.main.add(this.dellAll);
        this.dellAll.setBounds(240, 230, 170, 23);
        this.logScroll.setViewportView(this.log);
        this.main.add(this.logScroll);
        this.logScroll.setBounds(0, 290, 710, 150);
        this.dontJamp.setText("Не выпрыгивать из трея");
        this.main.add(this.dontJamp);
        this.dontJamp.setBounds(10, 200, 151, 23);
        this.jButton1.setText("Запустить потоки");
        this.jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.jButton1ActionPerformed(evt);
            }
        });
        this.main.add(this.jButton1);
        this.jButton1.setBounds(570, 230, 130, 23);
        this.mainPane.addTab("Ханюша", this.main);
        this.chans.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.chansActionPerformed(evt);
            }
        });
        this.jLabel5.setText("Брать треды с:");
        this.page.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.pageStateChanged(evt);
            }
        });
        this.page.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                GUI.this.pageKeyPressed(evt);
            }
        });
        this.jLabel7.setText("страницы");
        this.board.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.boardFocusLost(evt);
            }
        });
        this.jLabel13.setText("Доска:");
        this.jLabel8.setText("Потоков:");
        this.timeout.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.timeoutStateChanged(evt);
            }
        });
        this.timeout.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                GUI.this.timeoutKeyPressed(evt);
            }
        });
        this.thread.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                GUI.this.threadKeyPressed(evt);
            }
        });
        this.jLabel9.setText("Тред:");
        this.jLabel12.setText("Таймаут:");
        this.threads.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.threadsStateChanged(evt);
            }
        });
        this.threads.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                GUI.this.threadsKeyPressed(evt);
            }
        });
        this.silentBump.setText("Удалять пост(тихий бамп)");
        this.silentBump.setEnabled(false);
        this.silentBump.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.silentBumpActionPerformed(evt);
            }
        });
        this.msgCountInteger.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.msgCountIntegerStateChanged(evt);
            }
        });
        this.msgCountInteger.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                GUI.this.msgCountIntegerKeyPressed(evt);
            }
        });
        this.msgCount.setText("Не более");
        this.msgCount.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.msgCountMouseClicked(evt);
            }
        });
        this.jLabel15.setText("Сообщений");
        this.jLabel16.setText("UserAgent:");
        this.userAgent.setMaximumSize(new Dimension(6, 20));
        this.userAgent.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.userAgentFocusLost(evt);
            }
        });
        this.key.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.keyMouseClicked(evt);
            }
        });
        this.key.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.keyActionPerformed(evt);
            }
        });
        this.key.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.keyFocusLost(evt);
            }
        });
        this.key.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.keyKeyTyped(evt);
            }
        });
        this.ocrName.setText("Ключ:");
        this.ocrs.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                GUI.this.ocrsItemStateChanged(evt);
            }
        });
        this.modes.setModel(new DefaultComboBoxModel<>(new String[]{"Вайп", "Форс", "Держать на нулевой", "В тред"}));
        this.modes.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                GUI.this.modesItemStateChanged(evt);
            }
        });
        this.useParser.setText("Использовать парсер.");
        this.useParser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.useParserActionPerformed(evt);
            }
        });
        this.jLabel4.setText("Масштабирование капчи");
        this.jLabel33.setText("Width*");
        this.jLabel34.setText("Height*");
        this.scaleW.setText("1");
        this.scaleW.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.scaleWFocusLost(evt);
            }
        });
        this.scaleH.setText("1");
        this.scaleH.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.scaleHFocusLost(evt);
            }
        });
        this.reverseCaptcha.setText("Обратить цвета капчи на противоположные");
        this.reverseCaptcha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.reverseCaptchaActionPerformed(evt);
            }
        });
        this.dontTrayMsg.setText("Не выводить сообщения в трее");
        this.dontTrayMsg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.dontTrayMsgActionPerformed(evt);
            }
        });
        this.smartErrorHandler.setText("После");
        this.smartErrorHandler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.smartErrorHandlerActionPerformed(evt);
            }
        });
        this.smartErrorCount.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.smartErrorCountFocusLost(evt);
            }
        });
        this.smartErrorAction.setModel(new DefaultComboBoxModel<>(new String[]{"Выключать поток", "Перезапускать поток"}));
        this.smartErrorAction.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                GUI.this.smartErrorActionItemStateChanged(evt);
            }
        });
        this.jLabel35.setText("ошибок");
        this.waitForNotReady.setText("Ждать всех");
        this.waitForNotReady.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.waitForNotReadyActionPerformed(evt);
            }
        });
        GroupLayout settingsLayout = new GroupLayout(this.settings);
        this.settings.setLayout(settingsLayout);
        settingsLayout.setHorizontalGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(214, 214, 214).addComponent(this.ocrs, -2, 173, -2)).addGroup(settingsLayout.createSequentialGroup().addGap(10, 10, 10).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.useParser, -2, 140, -2).addComponent(this.silentBump).addGroup(settingsLayout.createSequentialGroup().addComponent(this.jLabel5).addGap(4, 4, 4).addComponent(this.page, -2, 40, -2).addGap(4, 4, 4).addComponent(this.jLabel7))).addGap(29, 29, 29).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.modes, -2, 173, -2).addComponent(this.chans, -2, 173, -2)).addGap(10, 10, 10).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addComponent(this.msgCount).addGap(2, 2, 2).addComponent(this.msgCountInteger, -2, 54, -2)).addGroup(settingsLayout.createSequentialGroup().addComponent(this.jLabel13).addGap(4, 4, 4).addComponent(this.board, -2, 51, -2))).addGap(10, 10, 10).addComponent(this.jLabel15)).addGroup(settingsLayout.createSequentialGroup().addContainerGap().addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel4).addGroup(settingsLayout.createSequentialGroup().addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(GroupLayout.Alignment.LEADING, settingsLayout.createSequentialGroup().addComponent(this.smartErrorHandler).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.smartErrorCount, -2, 37, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel35).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.smartErrorAction, -2, -1, -2)).addGroup(GroupLayout.Alignment.LEADING, settingsLayout.createSequentialGroup().addComponent(this.jLabel33).addGap(4, 4, 4).addComponent(this.scaleW, -2, 48, -2).addGap(4, 4, 4).addComponent(this.jLabel34).addGap(4, 4, 4).addComponent(this.scaleH, -2, 56, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.reverseCaptcha)).addComponent(this.waitForNotReady, GroupLayout.Alignment.LEADING)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.dontTrayMsg)))).addGroup(settingsLayout.createSequentialGroup().addGap(10, 10, 10).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addGroup(settingsLayout.createSequentialGroup().addComponent(this.jLabel16).addGap(4, 4, 4).addComponent(this.userAgent, -2, 278, -2).addGap(4, 4, 4).addComponent(this.jLabel12).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.timeout).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel8).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.threads, -2, 50, -2)).addGroup(GroupLayout.Alignment.LEADING, settingsLayout.createSequentialGroup().addComponent(this.ocrName).addGap(27, 27, 27).addComponent(this.key, -2, 278, -2).addGap(4, 4, 4).addComponent(this.jLabel9).addGap(18, 18, 18).addComponent(this.thread, -2, 194, -2))))).addContainerGap(68, 32767)));
        settingsLayout.setVerticalGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(41, 41, 41).addComponent(this.ocrs, -2, -1, -2).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addComponent(this.useParser).addComponent(this.silentBump).addGap(5, 5, 5).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel5)).addComponent(this.page, -2, -1, -2).addGroup(settingsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel7)))).addGroup(settingsLayout.createSequentialGroup().addGap(6, 6, 6).addComponent(this.modes, -2, -1, -2).addGap(6, 6, 6).addComponent(this.chans, -2, -1, -2)).addGroup(settingsLayout.createSequentialGroup().addGap(6, 6, 6).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.msgCount).addGroup(settingsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.msgCountInteger, -2, -1, -2))).addGap(6, 6, 6).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel13)).addComponent(this.board, -2, -1, -2))).addGroup(settingsLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(this.jLabel15))).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(11, 11, 11).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel16)).addComponent(this.userAgent, -2, -1, -2).addGroup(settingsLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel12)).addGroup(settingsLayout.createSequentialGroup().addGap(3, 3, 3).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.threads, -2, -1, -2).addComponent(this.jLabel8))))).addGroup(settingsLayout.createSequentialGroup().addGap(12, 12, 12).addComponent(this.timeout, -2, -1, -2))).addGap(6, 6, 6).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.ocrName)).addComponent(this.key, -2, -1, -2).addGroup(settingsLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel9)).addComponent(this.thread, -2, -1, -2)).addGap(32, 32, 32).addComponent(this.jLabel4).addGap(5, 5, 5).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(settingsLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel33)).addGroup(settingsLayout.createSequentialGroup().addGap(1, 1, 1).addComponent(this.scaleW, -2, -1, -2)).addGroup(settingsLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel34)).addGroup(settingsLayout.createSequentialGroup().addGap(1, 1, 1).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.scaleH, -2, -1, -2).addComponent(this.reverseCaptcha).addComponent(this.dontTrayMsg)))).addGap(18, 18, 18).addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.smartErrorHandler).addComponent(this.smartErrorCount, -2, -1, -2).addComponent(this.jLabel35).addComponent(this.smartErrorAction, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.waitForNotReady).addContainerGap(107, 32767)));
        this.mainPane.addTab("Общие настройки", this.settings);
        this.jScrollPane1.setViewportView(this.proxys);
        this.check.setText("Проверить выделенную");
        this.check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.checkActionPerformed(evt);
            }
        });
        this.checkAll.setText("Проверить все");
        this.checkAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.checkAllActionPerformed(evt);
            }
        });
        this.delete.setText("Удалить выделенные");
        this.delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.deleteActionPerformed(evt);
            }
        });
        this.checkOnLoad.setText("Проверять прокси при запуске программы");
        this.checkOnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.checkOnLoadActionPerformed(evt);
            }
        });
        this.checkContent.setText("Проверять передаваемый контент");
        this.checkContent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.checkContentActionPerformed(evt);
            }
        });
        this.noproxy.setText("Без прокси");
        this.noproxy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.noproxyActionPerformed(evt);
            }
        });
        this.load.setText("Взять из файла");
        this.load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.loadActionPerformed(evt);
            }
        });
        this.checkOnStart.setText("Эмулировать вайп");
        this.checkOnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.checkOnStartActionPerformed(evt);
            }
        });
        this.syte.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.syteFocusLost(evt);
            }
        });
        this.syte.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.syteKeyTyped(evt);
            }
        });
        this.syteKey.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.syteKeyFocusLost(evt);
            }
        });
        this.syteKey.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.syteKeyKeyTyped(evt);
            }
        });
        this.jLabel1.setText("Сайт для запроса контента:");
        this.jLabel32.setText("Ответ должен содержать:");
        GroupLayout proxyLayout = new GroupLayout(this.proxy);
        this.proxy.setLayout(proxyLayout);
        proxyLayout.setHorizontalGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(proxyLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -2, 246, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.checkOnStart).addGroup(proxyLayout.createSequentialGroup().addComponent(this.noproxy).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.load)).addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.checkContent, GroupLayout.Alignment.LEADING, -1, -1, 32767).addComponent(this.checkOnLoad, GroupLayout.Alignment.LEADING, -1, -1, 32767)).addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.proxyProgress, GroupLayout.Alignment.LEADING, -1, -1, 32767).addGroup(GroupLayout.Alignment.LEADING, proxyLayout.createSequentialGroup().addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.checkAll, GroupLayout.Alignment.LEADING, -1, -1, 32767).addComponent(this.check, GroupLayout.Alignment.LEADING, -1, 179, 32767).addComponent(this.delete, GroupLayout.Alignment.LEADING, -2, 187, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jLabel1).addComponent(this.syte).addComponent(this.syteKey).addComponent(this.jLabel32, -1, -1, 32767))))).addContainerGap()));
        proxyLayout.setVerticalGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(proxyLayout.createSequentialGroup().addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(proxyLayout.createSequentialGroup().addContainerGap().addComponent(this.check).addGap(18, 18, 18)).addGroup(GroupLayout.Alignment.TRAILING, proxyLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.jLabel1).addGap(5, 5, 5))).addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.checkAll).addComponent(this.syte, -2, -1, -2)).addGap(2, 2, 2).addComponent(this.jLabel32).addGap(2, 2, 2).addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.delete).addComponent(this.syteKey, -2, -1, -2)).addGap(18, 18, 18).addComponent(this.checkOnLoad).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.checkContent).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(proxyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.noproxy).addComponent(this.load)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.checkOnStart).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.proxyProgress, -2, -1, -2).addGap(142, 142, 142)).addComponent(this.jScrollPane1, -1, 442, 32767));
        this.mainPane.addTab("Прокси", this.proxy);
        this.all.setText("Всего копипасты: 0");
        this.reload.setText("Перезагрузить");
        this.reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.reloadActionPerformed(evt);
            }
        });
        this.empty.setText("Пустя строка");
        this.empty.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.emptyMouseClicked(evt);
            }
        });
        this.myseparator.setText("Свой символ");
        this.myseparator.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.myseparatorMouseClicked(evt);
            }
        });
        this.symbol.setText("-=-=-=-");
        this.symbol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.symbolActionPerformed(evt);
            }
        });
        this.symbol.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.symbolFocusLost(evt);
            }
        });
        this.randomize.setText("Рандомизировать");
        this.randomize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.randomizeActionPerformed(evt);
            }
        });
        this.fromfile.setText("Из файла...");
        this.fromfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.fromfileActionPerformed(evt);
            }
        });
        this.jLabel14.setText("Разделитель:");
        this.rndCount.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.rndCountStateChanged(evt);
            }
        });
        this.rndCount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.rndCountKeyTyped(evt);
            }
        });
        this.jLabel17.setText("Степень рандомизции:");
        this.replacements.setModel(new DefaultTableModel(new Object[0][], (Object[]) new String[]{"Что заменяем", "На что заменяем"}) {
            Class[] types = new Class[]{String.class, String.class};

            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
        });
        this.jScrollPane4.setViewportView(this.replacements);
        this.AddRrow.setText("Добавить строку");
        this.AddRrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.AddRrowActionPerformed(evt);
            }
        });
        this.RemoveRrow.setText("Удалить строку");
        this.RemoveRrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.RemoveRrowActionPerformed(evt);
            }
        });
        this.saver.setText("Сохранить замены");
        this.saver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.saverActionPerformed(evt);
            }
        });
        this.theme.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.themeFocusLost(evt);
            }
        });
        this.jLabel11.setText("Тема:");
        this.message.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.messageFocusLost(evt);
            }
        });
        this.jScrollPane2.setViewportView(this.message);
        this.name.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.nameFocusLost(evt);
            }
        });
        this.jLabel10.setText("Имя:");
        this.password.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.passwordFocusLost(evt);
            }
        });
        this.jLabel3.setText("Пароль:");
        this.email.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.emailFocusLost(evt);
            }
        });
        this.jLabel36.setText("Email:");
        GroupLayout pasteLayout = new GroupLayout(this.paste);
        this.paste.setLayout(pasteLayout);
        pasteLayout.setHorizontalGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pasteLayout.createSequentialGroup().addContainerGap().addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pasteLayout.createSequentialGroup().addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel11).addComponent(this.jLabel10).addComponent(this.jLabel3).addComponent(this.jLabel36)).addGap(18, 18, 18).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.email, -1, 161, 32767).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.password).addComponent(this.theme).addComponent(this.name, -1, 159, 32767)))).addComponent(this.randomize).addGroup(pasteLayout.createSequentialGroup().addComponent(this.myseparator).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.symbol, -2, 129, -2)).addComponent(this.all).addGroup(pasteLayout.createSequentialGroup().addComponent(this.reload).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.fromfile)).addComponent(this.empty).addComponent(this.jLabel14).addGroup(pasteLayout.createSequentialGroup().addComponent(this.jLabel17).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.rndCount, -2, 74, -2))).addGap(18, 18, 18).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pasteLayout.createSequentialGroup().addComponent(this.RemoveRrow, -2, 125, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.AddRrow).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.saver)).addComponent(this.jScrollPane4, -2, 463, -2))).addComponent(this.jScrollPane2, GroupLayout.Alignment.TRAILING, -1, 711, 32767));
        pasteLayout.setVerticalGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pasteLayout.createSequentialGroup().addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pasteLayout.createSequentialGroup().addContainerGap().addComponent(this.all).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.reload).addComponent(this.fromfile)).addGap(5, 5, 5).addComponent(this.jLabel14).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.empty).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.myseparator).addComponent(this.symbol, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.randomize).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel17).addComponent(this.rndCount, -2, -1, -2)).addGap(16, 16, 16).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel11).addComponent(this.theme, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.name, -2, -1, -2).addComponent(this.jLabel10)).addGap(10, 10, 10).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.password, -2, -1, -2).addComponent(this.jLabel3))).addGroup(pasteLayout.createSequentialGroup().addComponent(this.jScrollPane4, -2, 221, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.RemoveRrow).addComponent(this.AddRrow).addComponent(this.saver)))).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(pasteLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.email, -2, -1, -2).addComponent(this.jLabel36)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane2, -1, 133, 32767)));
        this.mainPane.addTab("Текст", this.paste);
        this.path.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.pathMouseClicked(evt);
            }
        });
        this.path.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                GUI.this.pathKeyPressed(evt);
            }
        });
        this.selectFileOrFolder.setText("...");
        this.selectFileOrFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.selectFileOrFolderActionPerformed(evt);
            }
        });
        this.noFile.setText("Без файла");
        this.noFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.noFileActionPerformed(evt);
            }
        });
        this.jLabel2.setText("Папка с пикчами:");
        this.picsPuck.setText("Не повторять пикчи");
        this.picsPuck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.picsPuckActionPerformed(evt);
            }
        });
        this.dontEdit.setText("Не изменять пикчи");
        this.dontEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.dontEditActionPerformed(evt);
            }
        });
        this.picsCount.setText("Всего пикчей в паке: ");
        this.one.setText("постить одну картинку");
        this.one.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.oneStateChanged(evt);
            }
        });
        this.one.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.oneActionPerformed(evt);
            }
        });
        this.useTmp.setText("постить через tmp");
        this.useTmp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.useTmpActionPerformed(evt);
            }
        });
        this.maxFileSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.maxFileSizeStateChanged(evt);
            }
        });
        this.maxFileSize.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.maxFileSizeKeyTyped(evt);
            }
        });
        this.jLabel18.setText("Максимальный размер в байтах:");
        this.jLabel19.setText("Отклонение по цвету:");
        this.colorRnd.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.colorRndStateChanged(evt);
            }
        });
        this.colorRnd.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.colorRndKeyTyped(evt);
            }
        });
        this.jLabel20.setText("Сколько пикселей менять:");
        this.pixelEdit.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.pixelEditStateChanged(evt);
            }
        });
        this.pixelEdit.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.pixelEditKeyTyped(evt);
            }
        });
        this.pasteOnPic.setText("Писать текст сообщения на пикчах");
        this.pasteOnPic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.pasteOnPicActionPerformed(evt);
            }
        });
        this.dontEditPixels.setText("Не изменять пиксели");
        this.dontEditPixels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.dontEditPixelsActionPerformed(evt);
            }
        });
        this.pasteNoMsg.setText("Не слать текстовые сообщения");
        this.pasteNoMsg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.pasteNoMsgActionPerformed(evt);
            }
        });
        this.picsTxtR.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.picsTxtRStateChanged(evt);
            }
        });
        this.picsTxtR.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.picsTxtRKeyTyped(evt);
            }
        });
        this.jLabel21.setText("R:");
        this.jLabel22.setText("G:");
        this.picsTxtG.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.picsTxtGStateChanged(evt);
            }
        });
        this.picsTxtG.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.picsTxtGKeyTyped(evt);
            }
        });
        this.jLabel23.setText("B:");
        this.picsTxtB.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.picsTxtBStateChanged(evt);
            }
        });
        this.picsTxtB.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.picsTxtBKeyTyped(evt);
            }
        });
        this.jLabel24.setText("Цвет надписи.");
        this.jLabel25.setText("Наносить текст с:");
        this.jLabel26.setText("X:");
        this.jLabel27.setText("Y:");
        this.startX.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.startXStateChanged(evt);
            }
        });
        this.startX.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.startXKeyTyped(evt);
            }
        });
        this.startY.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.startYStateChanged(evt);
            }
        });
        this.startY.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.startYKeyTyped(evt);
            }
        });
        this.colorRndRnd.setText("Случайно");
        this.colorRndRnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.colorRndRndActionPerformed(evt);
            }
        });
        this.jLabel28.setText("Межстрочное расстояние:");
        this.deltaY.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.deltaYStateChanged(evt);
            }
        });
        this.deltaY.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.deltaYKeyTyped(evt);
            }
        });
        this.FontName.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                GUI.this.FontNameFocusLost(evt);
            }
        });
        this.FontName.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.FontNameKeyTyped(evt);
            }
        });
        this.jLabel29.setText("Размер шрифта:");
        this.fontSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                GUI.this.fontSizeStateChanged(evt);
            }
        });
        this.fontSize.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                GUI.this.fontSizeKeyTyped(evt);
            }
        });
        this.jLabel30.setText("Название Шрифта:");
        this.randomBytes.setText("Случайные байты");
        this.randomBytes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.randomBytesActionPerformed(evt);
            }
        });
        this.randomPicGenerate.setText("random pic");
        this.randomPicGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.randomPicGenerateActionPerformed(evt);
            }
        });
        GroupLayout picsLayout = new GroupLayout(this.pics);
        this.pics.setLayout(picsLayout);
        picsLayout.setHorizontalGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2, -2, 222, -2).addGroup(picsLayout.createSequentialGroup().addGap(90, 90, 90).addComponent(this.path, -2, 272, -2))).addGap(8, 8, 8).addComponent(this.selectFileOrFolder, -2, 32, -2).addGap(8, 8, 8).addComponent(this.noFile).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.randomPicGenerate)).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.dontEdit).addGap(25, 25, 25).addComponent(this.picsCount, -2, 403, -2)).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.picsPuck).addGap(38, 38, 38).addComponent(this.dontEditPixels)).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.one).addGap(22, 22, 22).addComponent(this.useTmp)).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.jLabel18).addGap(10, 10, 10).addComponent(this.maxFileSize, -2, 147, -2)).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.jLabel19).addGap(10, 10, 10).addComponent(this.colorRnd, -2, 112, -2).addGap(11, 11, 11).addComponent(this.colorRndRnd).addGap(6, 6, 6).addComponent(this.randomBytes)).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.jLabel20).addGap(4, 4, 4).addComponent(this.pixelEdit, -2, 74, -2).addGap(13, 13, 13).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel30, -2, 107, -2).addGroup(picsLayout.createSequentialGroup().addGap(101, 101, 101).addComponent(this.FontName, -2, 218, -2)))).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.pasteOnPic).addComponent(this.pasteNoMsg)).addGroup(picsLayout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.jLabel21).addGap(4, 4, 4).addComponent(this.picsTxtR, -2, 60, -2).addGap(4, 4, 4).addComponent(this.jLabel22).addGap(4, 4, 4).addComponent(this.picsTxtG, -2, 64, -2).addGap(4, 4, 4).addComponent(this.jLabel23).addGap(4, 4, 4).addComponent(this.picsTxtB, -2, 59, -2).addGap(4, 4, 4).addComponent(this.jLabel24).addGap(6, 6, 6).addComponent(this.jLabel29).addGap(4, 4, 4).addComponent(this.fontSize, -2, 55, -2)).addGroup(picsLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(this.jLabel25)).addGroup(picsLayout.createSequentialGroup().addGap(10, 10, 10).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addGroup(picsLayout.createSequentialGroup().addComponent(this.jLabel27).addGap(10, 10, 10).addComponent(this.startY)).addGroup(GroupLayout.Alignment.LEADING, picsLayout.createSequentialGroup().addComponent(this.jLabel26).addGap(10, 10, 10).addComponent(this.startX, -2, 140, -2))).addGap(30, 30, 30).addComponent(this.jLabel28).addGap(16, 16, 16).addComponent(this.deltaY, -2, 68, -2))).addContainerGap(145, 32767)));
        picsLayout.setVerticalGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGap(10, 10, 10).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGap(5, 5, 5).addComponent(this.jLabel2)).addComponent(this.path, -2, -1, -2).addComponent(this.selectFileOrFolder).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.noFile).addComponent(this.randomPicGenerate))).addGap(7, 7, 7).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.dontEdit).addComponent(this.picsCount)).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.picsPuck).addComponent(this.dontEditPixels)).addGap(2, 2, 2).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.one).addComponent(this.useTmp)).addGap(6, 6, 6).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel18)).addComponent(this.maxFileSize, -2, -1, -2)).addGap(4, 4, 4).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel19)).addGroup(picsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.colorRnd, -2, -1, -2)).addComponent(this.colorRndRnd).addComponent(this.randomBytes)).addGap(27, 27, 27).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel20)).addGroup(picsLayout.createSequentialGroup().addGap(1, 1, 1).addComponent(this.pixelEdit, -2, -1, -2)).addGroup(picsLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel30)).addComponent(this.FontName, -2, -1, -2)).addGap(7, 7, 7).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pasteOnPic).addComponent(this.pasteNoMsg)).addGap(2, 2, 2).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(picsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel21)).addComponent(this.picsTxtR, -2, -1, -2).addGroup(picsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel22)).addComponent(this.picsTxtG, -2, -1, -2).addGroup(picsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel23)).addComponent(this.picsTxtB, -2, -1, -2).addGroup(picsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel24)).addGroup(picsLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel29)).addComponent(this.fontSize, -2, -1, -2)).addGap(6, 6, 6).addComponent(this.jLabel25).addGap(11, 11, 11).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel26).addComponent(this.startX, -2, -1, -2).addComponent(this.jLabel28).addComponent(this.deltaY, -2, -1, -2)).addGap(2, 2, 2).addGroup(picsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel27).addComponent(this.startY, -2, -1, -2))));
        this.mainPane.addTab("Пикчи", this.pics);
        this.scriptsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                GUI.this.scriptsListMouseClicked(evt);
            }
        });
        this.jScrollPane5.setViewportView(this.scriptsList);
        this.reLoad.setText("Перезагрузить");
        this.reLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.reLoadActionPerformed(evt);
            }
        });
        this.shutdown.setText("Выгрузить");
        this.shutdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.shutdownActionPerformed(evt);
            }
        });
        this.loadScript.setText("Загрузить");
        this.loadScript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.loadScriptActionPerformed(evt);
            }
        });
        this.dontUseScript.setText("Не использовать.");
        this.dontUseScript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.dontUseScriptActionPerformed(evt);
            }
        });
        GroupLayout scriptsLayout = new GroupLayout(this.scripts);
        this.scripts.setLayout(scriptsLayout);
        scriptsLayout.setHorizontalGroup(scriptsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(scriptsLayout.createSequentialGroup().addComponent(this.jScrollPane5, -2, 285, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(scriptsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(scriptsLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.loadScript, -1, -1, 32767).addComponent(this.shutdown, -1, -1, 32767).addComponent(this.reLoad, -1, -1, 32767)).addComponent(this.scriptInfo, -2, 317, -2).addComponent(this.dontUseScript)).addContainerGap(99, 32767)));
        scriptsLayout.setVerticalGroup(scriptsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane5, -1, 442, 32767).addGroup(scriptsLayout.createSequentialGroup().addContainerGap().addComponent(this.reLoad).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.shutdown).addGap(18, 18, 18).addComponent(this.loadScript).addGap(26, 26, 26).addComponent(this.dontUseScript).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 249, 32767).addComponent(this.scriptInfo, -2, 24, -2).addContainerGap()));
        this.mainPane.addTab("Скрипты", this.scripts);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.mainPane, -1, 716, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.mainPane, -2, 467, -2));
        pack();
    }

    public void reSelectOCR() {
        this.ocrs.setSelectedItem(this.tm.getScripts().getOCRbyName(Config.ocrMode));
    }

    public void addChan(String name) {
        this.chans.addItem(name);
    }

    public void showMessage(String msg, int type) {
        JOptionPane.showMessageDialog(this, msg, "Ханю", type);
    }

    public void openPane(int p) {
        this.mainPane.setSelectedIndex(p);
    }

    public void setPasteCount(int paste) {
        this.all.setText("Всего копипасты: " + String.valueOf(paste));
    }

    public void setSuccessful(int i) {
        if (Config.parser)
            this.dellAll.setEnabled((i > 0));
        this.suc.setText("Всего успешно:   " + String.valueOf(i));
    }

    public void setFailed(int i) {
        this.failed.setText("Всего неудачно: " + String.valueOf(i));
    }

    public void setThreadState(boolean state) {
        this.thread.setEnabled(state);
    }

    public void logInfo(String text) {
        try {
            this.logDocument.insertString(this.logDocument.getLength(), "[" + Utils.getTime(":") + "]" + text + "\n", type.Info.getAtributes());
            this.log.setCaretPosition(this.logDocument.getLength());
        } catch (Exception e) {
        }
    }

    public void logError(String text) {
        try {
            this.logDocument.insertString(this.logDocument.getLength(), "[" + Utils.getTime(":") + "]" + text + "\n", type.Error.getAtributes());
            this.log.setCaretPosition(this.logDocument.getLength());
        } catch (Exception e) {
        }
    }

    public int getThreads() {
        return ((Integer) this.threads.getValue()).intValue();
    }

    public void SwitchStartStop() {
        if (this.tm.isWork()) {
            start();
            setState(!this.tm.isWork());
            setUpState(!this.tm.isWork());
        } else {
            stop();
            setUpState(this.tm.isWork());
            setState(!this.tm.isWork());
        }
    }

    private void setState(boolean w) {
        this.suc.setVisible(!w);
        this.failed.setVisible(!w);
        this.message.setEnabled(w);
        this.mainPane.setEnabledAt(1, w);
        this.mainPane.setEnabledAt(2, w);
        this.mainPane.setEnabledAt(3, w);
        this.mainPane.setEnabledAt(4, w);
        this.mainPane.setEnabledAt(5, w);
    }

    private void start() {
        Config.threads = ((Integer) this.threads.getValue()).intValue();
        if (!checkThreads())
            return;
        Config.saveConfigParam("thread", this.thread.getText());
        this.start.setText("Остановить");
    }

    private boolean checkThreads() {
        if (Config.noProxy || this.tm.size() > 0)
            return true;
        int th = ((Integer) this.threads.getValue()).intValue();
        if (th > this.tm.getProxyManager().size() && !Config.noProxy) {
            JOptionPane.showMessageDialog(this, "Потоков не может быть больше чем проксей", "Ханю", 0);
            this.threads.setValue(Integer.valueOf(this.tm.getProxyManager().size()));
            return false;
        }
        if (th < 1) {
            JOptionPane.showMessageDialog(this, "Потоков не может быть ноль или отрицательное число", "Ханю", 0);
            this.threads.setValue(Integer.valueOf(this.tm.getProxyManager().size()));
            return false;
        }
        return true;
    }

    private void stop() {
        this.runThreads.setText("");
        this.ctxt.setText("");
        this.delSelected.setEnabled(false);
        this.stopSelected.setEnabled(false);
        this.suc.setText("Успешно:  0");
        this.suc.setVisible(false);
        this.failed.setText("Неудачно: 0");
        this.failed.setVisible(false);
        this.start.setText("Запустить");
    }

    private void setProxyState(Boolean b) {
        this.proxys.setEnabled(b.booleanValue());
        this.check.setEnabled(b.booleanValue());
        this.checkAll.setEnabled(b.booleanValue());
        this.delete.setEnabled(b.booleanValue());
        this.checkOnLoad.setEnabled(b.booleanValue());
        this.checkContent.setEnabled(b.booleanValue());
        this.checkOnStart.setEnabled(b.booleanValue());
    }

    public void reSelectChan() {
        this.chans.setSelectedIndex(this.tm.getChanManager().getIndexName(Config.chanName));
        setTitle("Ханюшечка, чан: " + Config.chanName + ", Доска: " + Config.board);
    }

    private void formWindowClosing(WindowEvent evt) {
    }

    private boolean checkPage() {
        if (((Integer) this.page.getValue()).intValue() < 0)
            return true;
        return false;
    }

    private void formWindowStateChanged(WindowEvent evt) {
        if (evt.getOldState() == 0)
            setVisible(false);
    }

    private void pasteNoMsgActionPerformed(ActionEvent evt) {
        Config.pasteNoMsg = this.pasteNoMsg.isSelected();
        Config.saveConfigParam("pasteNoMsg", Config.pasteNoMsg);
    }

    private void dontEditPixelsActionPerformed(ActionEvent evt) {
        Config.dontEditPixels = this.dontEditPixels.isSelected();
        Config.saveConfigParam("dontEditPixels", Config.dontEditPixels);
        this.colorRnd.setEnabled(!Config.dontEditPixels);
        this.pixelEdit.setEnabled(!Config.dontEditPixels);
        this.colorRndRnd.setEnabled(!Config.dontEditPixels);
    }

    private void pasteOnPicActionPerformed(ActionEvent evt) {
        Config.pasteOnPic = this.pasteOnPic.isSelected();
        this.pasteNoMsg.setEnabled(Config.pasteOnPic);
        this.picsTxtR.setEnabled(Config.pasteOnPic);
        this.picsTxtG.setEnabled(Config.pasteOnPic);
        this.picsTxtB.setEnabled(Config.pasteOnPic);
        this.fontSize.setEnabled(Config.pasteOnPic);
        this.startX.setEnabled(Config.pasteOnPic);
        this.startY.setEnabled(Config.pasteOnPic);
        this.deltaY.setEnabled(Config.pasteOnPic);
        this.FontName.setEnabled(Config.pasteOnPic);
        Config.saveConfigParam("pasteOnPic", Config.pasteOnPic);
    }

    private void pixelEditKeyTyped(KeyEvent evt) {
        int c = ((Integer) this.pixelEdit.getValue()).intValue();
        if (c < 1) {
            this.pixelEdit.setValue(Integer.valueOf(1));
            Config.pixelEdit = 1;
            Config.saveConfigParam("pixelEdit", 1);
            return;
        }
        Config.pixelEdit = c;
        Config.saveConfigParam("pixelEdit", c);
    }

    private void pixelEditStateChanged(ChangeEvent evt) {
        int c = ((Integer) this.pixelEdit.getValue()).intValue();
        if (c < 1) {
            this.pixelEdit.setValue(Integer.valueOf(1));
            Config.pixelEdit = 1;
            Config.saveConfigParam("pixelEdit", 1);
            return;
        }
        Config.pixelEdit = c;
        Config.saveConfigParam("pixelEdit", c);
    }

    private void colorRndKeyTyped(KeyEvent evt) {
        Config.colorRnd = ((Integer) this.colorRnd.getValue()).intValue();
        Config.saveConfigParam("colorRnd", Config.colorRnd);
    }

    private void colorRndStateChanged(ChangeEvent evt) {
        Config.colorRnd = ((Integer) this.colorRnd.getValue()).intValue();
        Config.saveConfigParam("colorRnd", Config.colorRnd);
    }

    private void maxFileSizeKeyTyped(KeyEvent evt) {
        int c = ((Integer) this.maxFileSize.getValue()).intValue();
        if (c < 1) {
            this.maxFileSize.setValue(Integer.valueOf(1));
            return;
        }
        Config.maxFileSize = c;
        Config.saveConfigParam("maxFileSize", c);
    }

    private void maxFileSizeStateChanged(ChangeEvent evt) {
        int c = ((Integer) this.maxFileSize.getValue()).intValue();
        if (c < 1) {
            this.maxFileSize.setValue(Integer.valueOf(1));
            return;
        }
        Config.maxFileSize = c;
        Config.saveConfigParam("maxFileSize", c);
    }

    private void useTmpActionPerformed(ActionEvent evt) {
        Config.useTmp = this.useTmp.isSelected();
        Config.saveConfigParam("useTmp", Config.useTmp);
        this.randomBytes.setEnabled(Config.useTmp);
    }

    private void oneActionPerformed(ActionEvent evt) {
        String p = this.path.getText();
        if (p.contains(".j") || p.contains(".p") || p.contains(".g")) {
            this.one.setSelected(true);
        } else {
            this.one.setSelected(false);
        }
        Config.onePic = this.one.isSelected();
        Config.saveConfigParam("onePic", Config.onePic);
    }

    private void oneStateChanged(ChangeEvent evt) {
    }

    private void dontEditActionPerformed(ActionEvent evt) {
        Config.picsNotEdit = this.dontEdit.isSelected();
        setPicsEnable(!Config.picsNotEdit);
        Config.saveConfigParam("picsNotEdit", Config.picsNotEdit);
    }

    private void picsPuckActionPerformed(ActionEvent evt) {
        Config.picsPuck = this.picsPuck.isSelected();
        Config.saveConfigParam("picsPuck", Config.picsPuck);
    }

    private void noFileActionPerformed(ActionEvent evt) {
        noFile();
    }

    private void noFile() {
        Config.BokuNoFile = this.noFile.isSelected();
        Config.saveConfigParam("BokuNoFile", Config.BokuNoFile);
        this.selectFileOrFolder.setEnabled(!Config.BokuNoFile);
        this.path.setEnabled(!Config.BokuNoFile);
        this.dontEdit.setEnabled(!Config.BokuNoFile);
        this.picsPuck.setEnabled(!Config.BokuNoFile);
        this.picsCount.setEnabled(!Config.BokuNoFile);
        this.maxFileSize.setEnabled(!Config.BokuNoFile);
        setPicsEnable((!Config.BokuNoFile && !Config.picsNotEdit));
    }

    private void selectFileOrFolderActionPerformed(ActionEvent evt) {
        this.chooser.setFileSelectionMode(2);
        this.chooser.setCurrentDirectory(new File("./pic/"));
        FileFilter filterJpg = new FileNameExtensionFilter("JPG", new String[]{"jpg,"});
        FileFilter filterPng = new FileNameExtensionFilter("PNG", new String[]{"png"});
        FileFilter filterGif = new FileNameExtensionFilter("GIF", new String[]{"gif"});
        this.chooser.addChoosableFileFilter(filterGif);
        this.chooser.addChoosableFileFilter(filterJpg);
        this.chooser.addChoosableFileFilter(filterPng);
        int returnVal = this.chooser.showOpenDialog(this);
        if (returnVal == 0) {
            File f = new File(this.chooser.getSelectedFile().getPath());
            this.path.setText(f.getAbsolutePath());
            if (f.isDirectory())
                this.picsCount.setText("Всего пикчей в паке: " + (f.listFiles()).length);
            Config.path = f.getAbsolutePath();
            this.tm.reBuildImgManager();
            Config.saveConfigParam("path", Config.path);
        }
    }

    private void pathKeyPressed(KeyEvent evt) {
    }

    private void pathMouseClicked(MouseEvent evt) {
    }

    private void rndCountKeyTyped(KeyEvent evt) {
        int c = ((Integer) this.rndCount.getValue()).intValue();
        if (c < 1) {
            this.rndCount.setValue(Integer.valueOf(1));
            Config.rndCount = 1;
            Config.saveConfigParam("rndCount", 1);
            return;
        }
        Config.rndCount = c;
        Config.saveConfigParam("rndCount", c);
    }

    public void addOCR(OCR o) {
        this.ocrs.addItem(o);
        logInfo("Added OCR: " + o.getInfo());
    }

    private void rndCountStateChanged(ChangeEvent evt) {
        int c = ((Integer) this.rndCount.getValue()).intValue();
        if (c < 1) {
            this.rndCount.setValue(Integer.valueOf(1));
            return;
        }
        Config.rndCount = c;
        Config.saveConfigParam("rndCount", c);
    }

    private void fromfileActionPerformed(ActionEvent evt) {
        this.chooser.setFileSelectionMode(0);
        this.chooser.setCurrentDirectory(new File("./ini/"));
        int returnVal = this.chooser.showOpenDialog(this);
        if (returnVal == 0) {
            try {
                this.tm.getCopyPasteManager().load(this.chooser.getSelectedFile().getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.all.setText("Всего копипасты: " + this.tm.getCopyPasteManager().size());
        }
    }

    private void randomizeActionPerformed(ActionEvent evt) {
        Config.randomizePaste = this.randomize.isSelected();
        this.rndCount.setEnabled(Config.randomizePaste);
        Config.saveConfigParam("randomizePaste", Config.randomizePaste);
    }

    private void symbolFocusLost(FocusEvent evt) {
        Config.separator = this.symbol.getText();
        Config.saveConfigParam("separator", Config.separator);
    }

    private void symbolActionPerformed(ActionEvent evt) {
    }

    private void myseparatorMouseClicked(MouseEvent evt) {
        this.empty.setSelected(false);
        Config.emptySeparator = this.empty.isSelected();
        this.symbol.setEnabled(!Config.emptySeparator);
        Config.saveConfigParam("emptySeparator", Config.emptySeparator);
    }

    private void emptyMouseClicked(MouseEvent evt) {
        Config.emptySeparator = this.empty.isSelected();
        this.myseparator.setSelected(!Config.emptySeparator);
        this.symbol.setEnabled(!Config.emptySeparator);
        Config.saveConfigParam("emptySeparator", Config.emptySeparator);
    }

    private void reloadActionPerformed(ActionEvent evt) {
        this.tm.getCopyPasteManager();
        this.tm.getCopyPasteManager().load("./ini/copypaste.txt");
        this.all.setText("Всего копипасты: " + this.tm.getCopyPasteManager().size());
    }

    private void loadActionPerformed(ActionEvent evt) {
        this.chooser.setFileSelectionMode(0);
        this.chooser.setCurrentDirectory(new File("./ini/"));
        int returnVal = this.chooser.showOpenDialog(this);
        if (returnVal == 0) {
            try {
                this.tm.getProxyManager().load(this.chooser.getSelectedFile().getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.pdlm.clear();
            for (HttpProxy p : this.tm.getProxyManager())
                addProxy(p);
        }
    }

    private void noproxyActionPerformed(ActionEvent evt) {
        setProxyState(Boolean.valueOf(!this.noproxy.isSelected()));
        Config.noProxy = this.noproxy.isSelected();
        Config.saveConfigParam("noProxy", Config.noProxy);
    }

    private void checkContentActionPerformed(ActionEvent evt) {
        Config.contentCheck = this.checkContent.isSelected();
        this.syte.setEnabled(Config.contentCheck);
        this.syteKey.setEnabled(Config.contentCheck);
        Config.saveConfigParam("contentCheck", Config.contentCheck);
    }

    private void checkOnLoadActionPerformed(ActionEvent evt) {
        Config.checkOnLoad = this.checkOnLoad.isSelected();
        Config.saveConfigParam("checkOnLoad", Config.checkOnLoad);
    }

    private void deleteActionPerformed(ActionEvent evt) {
        int i = JOptionPane.showConfirmDialog(this, "Точно удалить?", "Ханюша", 0);
        if (i == 0)
            for (Object o : this.proxys.getSelectedValues()) {
                HttpProxy p = (HttpProxy) o;
                this.pdlm.removeElement(p);
                this.tm.getProxyManager().remove(p);
                this.tm.getProxyManager().saveFile();
            }
    }

    public void removeProxy(HttpProxy p) {
        this.pdlm.removeElement(p);
    }

    private void checkAllActionPerformed(ActionEvent evt) {
        UI ui = this;
        this.proxyProgress.setMaximum(this.tm.getProxyManager().size());
        (this.tm.getProxyManager()).progress = 1;
        proxyCheck(this.proxys.getSelectedValues());
    }

    public void setProxyProgress(int i) {
        this.proxyProgress.setValue(i);
        this.proxyProgress.setString("Проверено " + i + " из " + this.proxyProgress.getMaximum());
        this.proxyProgress.setStringPainted(true);
    }

    private void checkActionPerformed(ActionEvent evt) {
        this.proxyProgress.setMaximum((this.proxys.getSelectedValues()).length);
        proxyCheck(this.proxys.getSelectedValues());
    }

    private void proxyCheck(final Object[] cproxys) {
        final UI ui = this;
        this.proxyProgress.setValue(0);
        (this.tm.getProxyManager()).progress = 1;
        Utils.startThread(new Runnable() {
            public void run() {
                for (Object o : cproxys)
                    GUI.this.tm.getProxyManager().check((HttpProxy) o, ui);
            }
        });
    }

    private void ocrsItemStateChanged(ItemEvent evt) {
        setOCR();
    }

    private void keyKeyTyped(KeyEvent evt) {
    }

    private void keyFocusLost(FocusEvent evt) {
        Config.keyForServices = this.key.getText();
        Config.saveConfigParam("keyForServices", Config.keyForServices);
    }

    private void keyActionPerformed(ActionEvent evt) {
    }

    private void keyMouseClicked(MouseEvent evt) {
    }

    private void msgCountMouseClicked(MouseEvent evt) {
        Config.msgCount = this.msgCount.isSelected();
        Config.saveConfigParam("msgCount", Config.msgCount);
        this.msgCountInteger.setEnabled(Config.msgCount);
    }

    private void msgCountIntegerKeyPressed(KeyEvent evt) {
        countmessage();
    }

    private void msgCountIntegerStateChanged(ChangeEvent evt) {
        countmessage();
    }

    public void updateThreadStatus() {
        this.runThreads.setText("[Поток: " + this.selectedWipe.toString() + "] [успешных: " + this.selectedWipe.getSuccessful() + "] [неудачных: " + this.selectedWipe.getFailed() + "][" + (this.wt.indexOf(this.selectedWipe) + 1) + " из " + this.wt.size() + " потоков]");
    }

    public void updateThreadStatus(AbstractWipe w) {
        this.runThreads.setText("[Поток: " + w.toString() + "] [успешных: " + w.getSuccessful() + "] [неудачных: " + w.getFailed() + "] [" + (this.wt.indexOf(w) + 1) + " из " + this.wt.size() + " потоков]");
    }

    private void silentBumpActionPerformed(ActionEvent evt) {
        Config.silentBump = this.silentBump.isSelected();
        Config.saveConfigParam("silentBump", Config.silentBump);
    }

    private void boardFocusLost(FocusEvent evt) {
        Config.board = this.board.getText();
        Config.saveConfigParam("board", Config.board);
        setTitle("Ханюшечка. Чан: " + Config.chanName + ", Доска: " + Config.board);
    }

    private void pageKeyPressed(KeyEvent evt) {
        if (checkPage())
            this.page.setValue(Integer.valueOf(0));
        Config.page = ((Integer) this.page.getValue()).intValue();
        Config.saveConfigParam("page", Config.page);
    }

    private void pageStateChanged(ChangeEvent evt) {
        if (checkPage())
            this.page.setValue(Integer.valueOf(0));
        Config.page = ((Integer) this.page.getValue()).intValue();
        Config.saveConfigParam("page", Config.page);
    }

    private void chansActionPerformed(ActionEvent evt) {
        if (!isVisible())
            return;
        Config.chanName = (String) this.chans.getSelectedItem();
        Config.saveConfigParam("chanName", Config.chanName);
        setTitle("Ханюшечка. Чан: " + Config.chanName + ", Доска: " + Config.board);
        this.tm.getChanManager().createAndLoad();
        myInit();
    }

    private void reAllActionPerformed(ActionEvent evt) {
        for (AbstractWipe wp : this.wt)
            updateCaptcha(wp);
    }

    private void sendActionPerformed(ActionEvent evt) {
        send();
    }

    private void reMeActionPerformed(ActionEvent evt) {
        if (this.send.isEnabled()) {
            this.ctxt.setText("");
            updateCaptcha(this.selectedWipe);
        }
    }

    private void setNextCaptcha() {
        Utils.startThread(new Runnable() {
            public void run() {
                GUI.this.captchas.setSelectedItem(GUI.this.getNextCaptcha(false));
            }
        });
    }

    private void setFormedCaptcha() {
        Utils.startThread(new Runnable() {
            public void run() {
                GUI.this.captchas.setSelectedItem(GUI.this.getFormedCaptcha());
            }
        });
    }

    private void ctxtKeyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case 10:
                send();
                return;
            case 116:
                this.ctxt.setText("");
                updateCaptcha(this.selectedWipe);
                return;
            case 39:
                if (this.wt.size() != 1)
                    setNextCaptcha();
                return;
            case 37:
                if (this.wt.size() != 1)
                    setFormedCaptcha();
                return;
        }
    }

    private void cimgMouseEntered(MouseEvent evt) {
        if (this.cimg.getIcon() != null && this.send.isEnabled()) {
            this.cimg.setCursor(handCursor);
        } else {
            this.cimg.setCursor(simpleCursor);
        }
    }

    private void cimgMouseClicked(MouseEvent evt) {
        if (this.send.isEnabled())
            updateCaptcha(this.selectedWipe);
    }

    private void captchasItemStateChanged(ItemEvent evt) {
        if (this.re) {
            this.re = false;
            return;
        }
        Utils.startThread(new Runnable() {
            public void run() {
                GUI.this.reMe.setEnabled(true);
                GUI.this.send.setEnabled(true);
                GUI.this.ctxt.setEnabled(true);
                GUI.this.ctxt.setText("");
                GUI.this.cimg.setText("");
                if (GUI.this.captchas.getSelectedItem() == null && !GUI.this.wt.isEmpty()) {
                    ww = GUI.this.wt.get(0);
                } else {
                    ww = (AbstractWipe) GUI.this.captchas.getSelectedItem();
                }
                if (ww == null)
                    return;
                if (ww.getCaptcha() != null && ww.getCaptcha().isEmpty())
                    GUI.this.ctxt.setText(ww.getCaptcha());
                GUI.this.selectedWipe = ww;
                if (!GUI.this.selectedWipe.isWork())
                    return;
                if (ww.getCaptchaImg() == null) {
                    GUI.this.setCaptcha(ww);
                } else {
                    GUI.this.cimg.setIcon(ww.getCaptchaImg());
                }
                GUI.this.runThreads.setText("[Поток: " + ww.toString() + "] [успешных: " + ww.getSuccessful() + "] [неудачных: " + ww.getFailed() + "] [" + (GUI.this.wt.indexOf(ww) + 1) + " из " + GUI.this.wt.size() + " потоков ]");
                GUI.this.cimg.setToolTipText(ww.getProxy().getHost());
                GUI.this.status.setText(ww.getStatus());
                GUI.this.threads.setToolTipText(ww.getProxy().getHost());
                AbstractWipe ww = null;
            }
        });
    }

    private void hanyuuMouseClicked(MouseEvent evt) {
        setVisible(false);
        Faggot.getinstance().display();
    }

    private void startActionPerformed(ActionEvent evt) {
        Config.msg = this.message.getText();
        Config.saveConfigParam("msg", Config.msg);
        Config.thread = this.thread.getText();
        Config.saveConfigParam("thread", Config.thread);
        if (!checkThreads())
            return;
        Utils.startThread(new Runnable() {
            public void run() {
                GUI.this.tm.StartStopManage();
            }
        });
    }

    private boolean checkRGBReange(int rgb, JSpinner js) {
        boolean r = (-1 < rgb && rgb < 255);
        if (!r)
            js.setValue(Integer.valueOf(0));
        return r;
    }

    private void picsTxtRStateChanged(ChangeEvent evt) {
        int rgb = ((Integer) this.picsTxtR.getValue()).intValue();
        if (!checkRGBReange(rgb, this.picsTxtR))
            rgb = 0;
        Config.picsTxtR = rgb;
        Config.saveConfigParam("picsTxtR", Config.picsTxtR);
    }

    private void picsTxtRKeyTyped(KeyEvent evt) {
        int rgb = ((Integer) this.picsTxtR.getValue()).intValue();
        if (!checkRGBReange(rgb, this.picsTxtR))
            rgb = 0;
        Config.picsTxtR = rgb;
        Config.saveConfigParam("picsTxtR", Config.picsTxtR);
    }

    public void seWindowtState(int state) {
        setState(state);
    }

    private void picsTxtGStateChanged(ChangeEvent evt) {
        int rgb = ((Integer) this.picsTxtG.getValue()).intValue();
        if (!checkRGBReange(rgb, this.picsTxtG))
            rgb = 0;
        Config.picsTxtG = rgb;
        Config.saveConfigParam("picsTxtG", Config.picsTxtG);
    }

    private void picsTxtGKeyTyped(KeyEvent evt) {
        int rgb = ((Integer) this.picsTxtG.getValue()).intValue();
        if (!checkRGBReange(rgb, this.picsTxtG))
            rgb = 0;
        Config.picsTxtG = rgb;
        Config.saveConfigParam("picsTxtG", Config.picsTxtG);
    }

    private void picsTxtBStateChanged(ChangeEvent evt) {
        int rgb = ((Integer) this.picsTxtB.getValue()).intValue();
        if (!checkRGBReange(rgb, this.picsTxtB))
            rgb = 0;
        Config.picsTxtB = rgb;
        Config.saveConfigParam("picsTxtB", Config.picsTxtB);
    }

    private void picsTxtBKeyTyped(KeyEvent evt) {
        int rgb = ((Integer) this.picsTxtB.getValue()).intValue();
        if (!checkRGBReange(rgb, this.picsTxtB))
            rgb = 0;
        Config.picsTxtB = rgb;
        Config.saveConfigParam("picsTxtB", Config.picsTxtB);
    }

    private void startXKeyTyped(KeyEvent evt) {
        int x = ((Integer) this.startX.getValue()).intValue();
        if (x < 0)
            x = 0;
        this.startX.setValue(Integer.valueOf(x));
        Config.startX = x;
        Config.saveConfigParam("startX", x);
    }

    private void startXStateChanged(ChangeEvent evt) {
        int x = ((Integer) this.startX.getValue()).intValue();
        if (x < 0)
            x = 0;
        this.startX.setValue(Integer.valueOf(x));
        Config.startX = x;
        Config.saveConfigParam("startX", x);
    }

    private void startYStateChanged(ChangeEvent evt) {
        int y = ((Integer) this.startY.getValue()).intValue();
        if (y < 0)
            y = 0;
        this.startY.setValue(Integer.valueOf(y));
        Config.startY = y;
        Config.saveConfigParam("startY", y);
    }

    private void startYKeyTyped(KeyEvent evt) {
        int y = ((Integer) this.startY.getValue()).intValue();
        if (y < 0)
            y = 0;
        this.startY.setValue(Integer.valueOf(y));
        Config.startY = y;
        Config.saveConfigParam("startY", y);
    }

    private void deltaYKeyTyped(KeyEvent evt) {
        int y = ((Integer) this.deltaY.getValue()).intValue();
        if (y < 0)
            y = 0;
        this.deltaY.setValue(Integer.valueOf(y));
        Config.deltaY = y;
        Config.saveConfigParam("deltaY", y);
    }

    private void deltaYStateChanged(ChangeEvent evt) {
        int y = ((Integer) this.deltaY.getValue()).intValue();
        if (y < 0)
            y = 0;
        this.deltaY.setValue(Integer.valueOf(y));
        Config.deltaY = y;
        Config.saveConfigParam("deltaY", y);
    }

    private void FontNameKeyTyped(KeyEvent evt) {
        Config.FontName = this.FontName.getText();
        Config.saveConfigParam("FontName", Config.FontName);
    }

    private void FontNameFocusLost(FocusEvent evt) {
        Config.FontName = this.FontName.getText();
        Config.saveConfigParam("FontName", Config.FontName);
    }

    private void fontSizeStateChanged(ChangeEvent evt) {
        int s = ((Integer) this.fontSize.getValue()).intValue();
        if (s < 1)
            s = 1;
        this.fontSize.setValue(Integer.valueOf(s));
        Config.fontSize = s;
        Config.saveConfigParam("fontSize", s);
    }

    private void fontSizeKeyTyped(KeyEvent evt) {
        int s = ((Integer) this.fontSize.getValue()).intValue();
        if (s < 1)
            s = 1;
        this.fontSize.setValue(Integer.valueOf(s));
        Config.fontSize = s;
        Config.saveConfigParam("fontSize", s);
    }

    private void colorRndRndActionPerformed(ActionEvent evt) {
        Config.colorRndRnd = this.colorRndRnd.isSelected();
        Config.saveConfigParam("colorRndRnd", Config.colorRndRnd);
        this.colorRnd.setEnabled(!Config.colorRndRnd);
    }

    private void ontopActionPerformed(ActionEvent evt) {
        setAlwaysOnTop(this.ontop.isSelected());
    }

    private void formFocusGained(FocusEvent evt) {
        this.ctxt.requestFocus();
    }

    private void mainMouseClicked(MouseEvent evt) {
        this.ctxt.requestFocus();
    }

    private void checkOnStartActionPerformed(ActionEvent evt) {
        Config.emulateWipe = this.checkOnStart.isSelected();
        Config.saveConfigParam("emulateWipe", Config.emulateWipe);
    }

    private void threadsKeyPressed(KeyEvent evt) {
        if (checkThreads()) {
            Config.threads = ((Integer) this.threads.getValue()).intValue();
            Config.saveConfigParam("threads", Config.threads);
        }
    }

    private void threadsStateChanged(ChangeEvent evt) {
        if (checkThreads()) {
            Config.threads = ((Integer) this.threads.getValue()).intValue();
            Config.saveConfigParam("threads", Config.threads);
        }
    }

    private void timeoutStateChanged(ChangeEvent evt) {
        int t = ((Integer) this.timeout.getValue()).intValue();
        if (t < 0)
            this.timeout.setValue(Integer.valueOf(0));
        t = ((Integer) this.timeout.getValue()).intValue();
        Config.timeout = t;
        Config.saveConfigParam("timeOut", t);
    }

    private void timeoutKeyPressed(KeyEvent evt) {
        int t = ((Integer) this.timeout.getValue()).intValue();
        if (t < 0)
            this.timeout.setValue(Integer.valueOf(0));
        t = ((Integer) this.timeout.getValue()).intValue();
        Config.timeout = t;
        Config.saveConfigParam("timeOut", t);
    }

    private void mainFocusLost(FocusEvent evt) {
    }

    private void nameFocusLost(FocusEvent evt) {
        Config.name = this.name.getText();
        Config.saveConfigParam("name", Config.name);
    }

    private void themeFocusLost(FocusEvent evt) {
        Config.theme = this.theme.getText();
        Config.saveConfigParam("theme", Config.theme);
    }

    private void userAgentFocusLost(FocusEvent evt) {
        Config.userAgent = this.userAgent.getText();
        Config.saveConfigParam("userAgent", Config.userAgent);
    }

    private void passwordFocusLost(FocusEvent evt) {
        Config.password = this.password.getText();
        Config.saveConfigParam("password", Config.password);
    }

    private void randomBytesActionPerformed(ActionEvent evt) {
        Config.randomBytes = this.randomBytes.isSelected();
        Config.saveConfigParam("randomBytes", Config.randomBytes);
    }

    private void syteFocusLost(FocusEvent evt) {
        syte();
    }

    private void syteKeyFocusLost(FocusEvent evt) {
        key();
    }

    private void key() {
        if (this.syteKey.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Не указан ожидаемый ответ от сайта " + Config.syte, "Ханюша", 0);
            this.mainPane.setSelectedIndex(2);
            return;
        }
        Config.syteKey = this.syteKey.getText();
        Config.saveConfigParam("syteKey", Config.syteKey);
    }

    private void syte() {
        if (this.syte.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Не указан сайт.", "Ханюша", 0);
            this.mainPane.setSelectedIndex(2);
            return;
        }
        Config.syte = this.syte.getText();
        Config.saveConfigParam("syte", Config.syte);
    }

    private void syteKeyTyped(KeyEvent evt) {
        syte();
    }

    private void syteKeyKeyTyped(KeyEvent evt) {
        key();
    }

    private void dontTrayMsgActionPerformed(ActionEvent evt) {
        Config.dontTrayMsg = this.dontTrayMsg.isSelected();
        Config.saveConfigParam("dontTrayMsg", Config.dontTrayMsg);
    }

    private void nextCaptchaActionPerformed(ActionEvent evt) {
        this.captchas.setSelectedItem(getNextCaptcha(false));
    }

    private void formerCaptchaActionPerformed(ActionEvent evt) {
        this.captchas.setSelectedItem(getFormedCaptcha());
    }

    private void delSelectedActionPerformed(ActionEvent evt) {
        final UI ui = this;
        Utils.startThread(new Runnable() {
            public void run() {
                GUI.this.selectedWipe.destroy("Удалено пользователем.");
                GUI.this.removeThread((AbstractWipe) null);
                GUI.this.captchas.setSelectedItem(GUI.this.getNextCaptcha(true));
                GUI.this.updateThreadStatus();
                GUI.this.tm.getProxyManager().delete(GUI.this.selectedWipe.getProxy(), ui);
            }
        });
    }

    private void stopSelectedActionPerformed(ActionEvent evt) {
        Utils.startThread(new Runnable() {
            public void run() {
                GUI.this.selectedWipe.destroy("Остановлено пользователем.");
                GUI.this.removeThread((AbstractWipe) null);
                GUI.this.captchas.setSelectedItem(GUI.this.getNextCaptcha(true));
                GUI.this.updateThreadStatus();
            }
        });
    }

    private void threadKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            Config.thread = this.thread.getText();
            Config.saveConfigParam("thread", Config.thread);
        }
    }

    private void AddRrowActionPerformed(ActionEvent evt) {
        this.dtm.setRowCount(this.dtm.getRowCount() + 1);
    }

    private void RemoveRrowActionPerformed(ActionEvent evt) {
        this.dtm.removeRow(this.replacements.getSelectedRow());
        sr();
    }

    private void sr() {
        Config.tr = "";
        if (this.dtm.getRowCount() > 0)
            for (int i = 0; i < this.dtm.getRowCount(); i++)
                Config.tr += this.dtm.getValueAt(i, 0).toString() + ":" + this.dtm.getValueAt(i, 1).toString() + ";";
        Config.saveConfigParam("replacements", Config.tr);
        Config.buildReplacements();
    }

    private void saverActionPerformed(ActionEvent evt) {
        sr();
    }

    private void randomPicGenerateActionPerformed(ActionEvent evt) {
        Config.randomPicGenerate = this.randomPicGenerate.isSelected();
        Config.saveConfigParam("randomPicGenerate", Config.randomPicGenerate);
        Config.useTmp = !Config.randomPicGenerate;
        Config.saveConfigParam("useTmp", Config.useTmp);
        this.useTmp.setSelected(Config.useTmp);
    }

    private void shutdownActionPerformed(ActionEvent evt) {
        int index = this.scriptsList.getSelectedIndex();
        ScriptContainer s = this.sdlm.get(index);
        this.sdlm.remove(index);
        this.tm.getScripts().getScripts().remove(s);
    }

    private void reLoadActionPerformed(ActionEvent evt) {
        int index = this.scriptsList.getSelectedIndex();
        ScriptContainer s = this.sdlm.get(index);
        this.sdlm.remove(index);
        this.tm.getScripts().reLoad(s);
    }

    private void scriptsListMouseClicked(MouseEvent evt) {
        int index = this.scriptsList.getSelectedIndex();
        if (index > -1) {
            ScriptContainer s = this.sdlm.get(index);
            this.dontUseScript.setSelected(!s.use);
        }
    }

    private void dontUseScriptActionPerformed(ActionEvent evt) {
        int index = this.scriptsList.getSelectedIndex();
        if (index > -1) {
            ScriptContainer s = this.sdlm.get(index);
            s.use = !this.dontUseScript.isSelected();
        }
    }

    private void loadScriptActionPerformed(ActionEvent evt) {
        this.chooser.setFileSelectionMode(0);
        this.chooser.setCurrentDirectory(new File("./scripts/"));
        int returnVal = this.chooser.showOpenDialog(this);
        if (returnVal == 0)
            this.tm.getScripts().load(this.chooser.getSelectedFile());
    }

    private void modesItemStateChanged(ItemEvent evt) {
        switch (this.modes.getSelectedIndex()) {
            case 0:
                Config.workMode = WorkMode.WipeBoard;
                this.page.setEnabled(false);
                this.thread.setEnabled(false);
                break;
            case 1:
                Config.workMode = WorkMode.Force;
                this.page.setEnabled(true);
                this.thread.setEnabled(false);
                break;
            case 2:
                Config.workMode = WorkMode.OnZeroPage;
                this.page.setEnabled(false);
                this.thread.setEnabled(true);
                break;
            case 3:
                Config.workMode = WorkMode.WipeThread;
                this.page.setEnabled(false);
                this.thread.setEnabled(true);
                break;
        }
        Config.saveConfigParam("workMode", Config.workMode.toString());
    }

    private void useParserActionPerformed(ActionEvent evt) {
        Config.parser = this.useParser.isSelected();
        Config.saveConfigParam("parser", Config.parser);
        this.silentBump.setEnabled(Config.parser);
        this.dellAll.setEnabled(Config.parser);
    }

    private void scaleWFocusLost(FocusEvent evt) {
        Config.scaleW = Double.valueOf(this.scaleW.getText()).doubleValue();
        Config.saveConfigParam("scaleW", Config.scaleW);
    }

    private void scaleHFocusLost(FocusEvent evt) {
        Config.scaleH = Double.valueOf(this.scaleH.getText()).doubleValue();
        Config.saveConfigParam("scaleH", Config.scaleH);
    }

    private void dellAllActionPerformed(ActionEvent evt) {
        this.tm.deleteAllMessages();
    }

    private void reverseCaptchaActionPerformed(ActionEvent evt) {
        Config.reverseCaptcha = this.reverseCaptcha.isSelected();
        Config.saveConfigParam("reverseCaptcha", Config.reverseCaptcha);
    }

    private void emailFocusLost(FocusEvent evt) {
        Config.email = this.email.getText();
        Config.saveConfigParam("email", Config.email);
    }

    private void smartErrorHandlerActionPerformed(ActionEvent evt) {
        Config.smartErrorHandler = this.smartErrorHandler.isSelected();
        Config.saveConfigParam("smartErrorHandler", Config.smartErrorHandler);
        this.smartErrorCount.setEnabled(Config.smartErrorHandler);
        this.smartErrorAction.setEnabled(Config.smartErrorHandler);
    }

    private void smartErrorCountFocusLost(FocusEvent evt) {
        int errors;
        try {
            errors = Integer.parseInt(this.smartErrorCount.getText());
        } catch (Exception e) {
            showMessage("Вы ввели не число.", 0);
            return;
        }
        Config.smartErrorCount = errors;
        Config.saveConfigParam("smartErrorCount", Config.smartErrorCount);
    }

    private void smartErrorActionItemStateChanged(ItemEvent evt) {
        Config.smartErrorAction = this.smartErrorAction.getSelectedIndex();
        Config.saveConfigParam("smartErrorAction", Config.smartErrorAction);
    }

    private void waitForNotReadyActionPerformed(ActionEvent evt) {
        Config.waitForNotReady = this.waitForNotReady.isSelected();
        Config.saveConfigParam("waitForNotReady", Config.waitForNotReady);
    }

    private void messageFocusLost(FocusEvent evt) {
        Config.msg = this.message.getText();
        Config.saveConfigParam("msg", Config.msg);
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        this.tm.runThreads();
    }

    public void addScript(ScriptContainer s) {
        this.sdlm.addElement(s);
        logInfo("Added script: " + s.script.getInfo());
    }

    private void updateCaptcha(final AbstractWipe ww) {
        Utils.startThread(new Runnable() {
            public void run() {
                try {
                    GUI.this.cimg.setIcon((Icon) null);
                    GUI.this.cimg.setText("Обновляем...");
                    InputStream in = CaptchaUtils.getScaledCaptchaStream(Config.scaleW, Config.scaleH, ww);
                    GUI.this.cimg.setText("");
                    GUI.this.captchaImage = new ImageIcon(ImageIO.read(in));
                    in.close();
                    if (GUI.this.selectedWipe == ww) {
                        GUI.this.cimg.setIcon(GUI.this.captchaImage);
                    } else {
                        ww.setCaptchaImg(GUI.this.captchaImage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void send() {
        if (this.ctxt.getText().isEmpty() || this.cimg.getIcon() == null)
            return;
        Utils.startThread(new Runnable() {
            public void run() {
                AbstractWipe ww = GUI.this.selectedWipe;
                GUI.this.setUpState(false);
                ww.setCaptchaImg(null);
                ww.setStatus("Посылаем..");
                GUI.this.setStatus(ww);
                ww.setCaptcha(GUI.this.ctxt.getText());
                GUI.this.ctxt.setText("");
                if (GUI.this.captchas.getItemCount() > 1)
                    GUI.this.captchas.setSelectedItem(GUI.this.getNextCaptcha(true));
                ww.myNotify();
            }
        });
    }

    public void setStatus(AbstractWipe ww) {
        if (this.selectedWipe == ww)
            this.status.setText(ww.getStatus());
    }

    private AbstractWipe getNextCaptcha(boolean check) {
        if (this.ctxt.getText().isEmpty() && check)
            getNextCaptcha(false);
        int m = this.wt.indexOf(this.selectedWipe) + 1;
        if (m == this.wt.size())
            return this.wt.get(0);
        return this.wt.get(m++);
    }

    private AbstractWipe getFormedCaptcha() {
        int m = this.wt.indexOf(this.selectedWipe);
        if (m == 0) {
            System.out.println(m + ":" + this.wt.size() + ":" + this.wt.indexOf(this.selectedWipe) + ":" + this.wt.get(this.wt.size() - 1));
            return this.wt.get(this.wt.size() - 1);
        }
        System.out.println(m + ":" + this.wt.size() + ":" + this.wt.indexOf(this.selectedWipe) + ":" + this.wt.get(m--));
        return this.wt.get(m--);
    }

    private void setPicsEnable(boolean b) {
        this.one.setEnabled(b);
        this.useTmp.setEnabled(b);
        this.colorRnd.setEnabled((b && !Config.dontEditPixels && !Config.colorRndRnd));
        this.pixelEdit.setEnabled((b && !Config.dontEditPixels));
        this.colorRndRnd.setEnabled((b && !Config.dontEditPixels));
        this.pasteOnPic.setEnabled(b);
        this.randomBytes.setEnabled(b);
        this.pasteNoMsg.setEnabled((b && Config.pasteOnPic));
        this.pasteNoMsg.setEnabled((b && Config.pasteOnPic));
        this.picsTxtR.setEnabled((b && Config.pasteOnPic));
        this.picsTxtG.setEnabled((b && Config.pasteOnPic));
        this.picsTxtB.setEnabled((b && Config.pasteOnPic));
        this.dontEditPixels.setEnabled(b);
        this.startX.setEnabled((b && Config.pasteOnPic));
        this.startY.setEnabled((b && Config.pasteOnPic));
        this.deltaY.setEnabled((b && Config.pasteOnPic));
        this.fontSize.setEnabled((b && Config.pasteOnPic));
        this.FontName.setEnabled((b && Config.pasteOnPic));
    }

    private void countmessage() {
        int count = ((Integer) this.msgCountInteger.getValue()).intValue();
        if (count < 1) {
            this.msgCountInteger.setValue(Integer.valueOf(1));
            return;
        }
        Config.msgCountInt = count;
        Config.saveConfigParam("msgCountInt", count);
    }

    public void setCaptcha(AbstractWipe ww) {
        try {
            this.cimg.setText("");
            if (!this.tm.isWork() || ww == null || !ww.isWork())
                return;
            if (!isVisible() && !this.dontJamp.isSelected()) {
                this.fromTray = true;
                setVisible(true);
                setState(0);
            }
            if (!this.wt.contains(ww)) {
                this.re = true;
                this.captchas.addItem(ww);
                this.wt.add(ww);
                this.selectedWipe = (AbstractWipe) this.captchas.getSelectedItem();
                if (this.selectedWipe == null)
                    return;
                if (this.wt.size() == this.tm.size()) {
                    this.captchas.setSelectedIndex(this.wt.indexOf(ww));
                    this.selectedWipe = (AbstractWipe) this.captchas.getSelectedItem();
                }
            }
            if (this.wt.size() > 1) {
                this.nextCaptcha.setEnabled(true);
                this.formerCaptcha.setEnabled(true);
            }
            this.runThreads.setText("[Поток: " + this.selectedWipe.toString() + "] [успешных: " + this.selectedWipe.getSuccessful() + "] [неудачных: " + this.selectedWipe.getFailed() + "] [" + (this.wt.indexOf(ww) + 1) + " из " + this.wt.size() + " потоков]");
            this.status.setText("");
            this.cimg.setText("");
            if (ww.getCaptchaImg() != null) {
                this.captchaImage = ww.getCaptchaImg();
                return;
            }
            if (this.selectedWipe == ww) {
                this.cimg.setIcon((Icon) null);
                this.cimg.setText("Обновляем капчу..");
            }
            InputStream in = CaptchaUtils.getScaledCaptchaStream(Config.scaleW, Config.scaleH, ww);
            if (in == null) {
                ww.destroy("No InputCaptcha");
                this.cimg.setText("");
            }
            this.captchaImage = new ImageIcon(ImageIO.read(in));
            in.close();
            ww.setCaptchaImg(this.captchaImage);
            if (this.selectedWipe.getProxy().getHost().contains(ww.getProxy().getHost())) {
                this.cimg.setIcon(this.captchaImage);
                this.cimg.setText("");
                this.cimg.setToolTipText(ww.getProxy().getHost());
            }
            setUpState(true);
        } catch (Exception e) {
            e.printStackTrace();
            this.cimg.setIcon((Icon) null);
            this.cimg.setText("");
            this.tm.getProxyManager().delete(ww.getProxy(), this);
            ww.destroy(e.toString());
        }
    }

    public void setUpState(boolean b) {
        if (!b)
            this.cimg.setIcon((Icon) null);
        this.reMe.setEnabled(b);
        this.send.setEnabled(b);
        this.reAll.setEnabled(b);
        this.ctxt.setEnabled(b);
        this.ctxt.requestFocus();
        this.status.setText("");
    }

    public void removeThread(AbstractWipe w) {
        this.captchas.removeItem(w);
        this.wt.remove(w);
        if (this.captchas.getItemCount() > 1)
            this.captchas.setSelectedItem(getNextCaptcha(true));
        this.nextCaptcha.setEnabled((this.wt.size() != 1));
        this.formerCaptcha.setEnabled((this.wt.size() != 1));
        if (this.wt.isEmpty()) {
            this.cimg.setIcon((Icon) null);
            this.send.setEnabled(false);
        }
    }

    public void addProxy(HttpProxy p) {
        this.pdlm.add(this.pdlm.size(), p);
    }

    private void setOCR() {
        if (!isVisible())
            return;
        Config.ocrMode = this.ocrs.getSelectedItem().toString();
        Config.saveConfigParam("ocrMode", Config.ocrMode);
    }
}
