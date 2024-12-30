import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        // Ana çerçeve
        JFrame cerceve = new JFrame("Sistem Bilgisi ve Ortam Değişkenleri");
        cerceve.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cerceve.setSize(1200, 800);

        // Ana panel
        JPanel anaPanel = new JPanel();
        anaPanel.setLayout(new BorderLayout());

        // Sekme paneli
        JTabbedPane sekmePaneli = new JTabbedPane();

        // Sistem Bilgileri Sekmesi
        JTextArea sistemBilgileriMetin = new JTextArea();
        JButton sistemBilgileriButon = new JButton("Sistem Bilgilerini Göster");
        sekmePaneli.addTab("Sistem Bilgileri", createPanelWithButton(sistemBilgileriMetin, sistemBilgileriButon, e -> sistemBilgileriniGoster(sistemBilgileriMetin)));

        // Ortam Değişkenleri Sekmesi
        JTextArea ortamDegiskenleriMetin = new JTextArea();
        JButton ortamDegiskenleriButon = new JButton("Ortam Değişkenlerini Göster");
        sekmePaneli.addTab("Ortam Değişkenleri", createPanelWithButton(ortamDegiskenleriMetin, ortamDegiskenleriButon, e -> ortamDegiskenleriniGoster(ortamDegiskenleriMetin)));

        // Açık Portlar ve Servisler Sekmesi
        JTextArea portlarMetin = new JTextArea();
        JButton portlarButon = new JButton("Portları Göster");
        sekmePaneli.addTab("Açık Portlar ve Servisler", createPanelWithButton(portlarMetin, portlarButon, e -> komutCalistir("netstat -ano", portlarMetin)));

        // Yüklü Yazılımlar Sekmesi
        JTextArea yukluYazilimlarMetin = new JTextArea();
        JButton yukluYazilimlarButon = new JButton("Yüklü Yazılımları Göster");
        sekmePaneli.addTab("Yüklü Yazılımlar", createPanelWithButton(yukluYazilimlarMetin, yukluYazilimlarButon, e -> komutCalistir("powershell Get-ItemProperty HKLM:\\Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\* | Select-Object DisplayName, DisplayVersion, Publisher", yukluYazilimlarMetin)));

        // CMD ve Powershell Versiyon Sekmesi
        JTextArea versiyonMetin = new JTextArea();
        JButton versiyonButon = new JButton("Versiyonları Göster");
        sekmePaneli.addTab("CMD ve Powershell Versiyon", createPanelWithButton(versiyonMetin, versiyonButon, e -> versiyonlariGoster(versiyonMetin)));

        // Active Directory Sekmesi
        JButton activeDirectoryButon = new JButton("Active Directory Yönetimi");
        sekmePaneli.addTab("Active Directory", createActiveDirectoryTab(activeDirectoryButon));

        // Şifreler ve Kimlik Bilgileri Sekmesi
        JTextArea sifrelerMetin = new JTextArea();
        JButton sifrelerButon = new JButton("Şifreleri Göster");
        sekmePaneli.addTab("Şifreler ve Kimlik Bilgileri", createPanelWithButton(sifrelerMetin, sifrelerButon, e -> sifreleriGoster(sifrelerMetin)));

        // Kullanıcılar ve Özellikleri Sekmesi
        JTextArea kullanicilarMetin = new JTextArea();
        JButton kullanicilarButon = new JButton("Kullanıcıları Göster");
        sekmePaneli.addTab("Kullanıcılar ve Özellikleri", createPanelWithButton(kullanicilarMetin, kullanicilarButon, e -> komutCalistir("whoami /all", kullanicilarMetin)));

        // Sekme panelini ana panele ekle
        anaPanel.add(sekmePaneli, BorderLayout.CENTER);

        // Çerçeveye ana paneli ekle
        cerceve.add(anaPanel);

        // Çerçeveyi görünür yap
        cerceve.setVisible(true);
    }

    // Panel oluşturma metodu
    private static JPanel createPanelWithButton(JTextArea metinAlani, JButton buton, ActionListener action) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        metinAlani.setEditable(false);
        JScrollPane kaydir = new JScrollPane(metinAlani);
        panel.add(kaydir, BorderLayout.CENTER);

        JPanel butonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        butonPanel.add(buton);
        JButton temizleButon = new JButton("Temizle");
        temizleButon.addActionListener(e -> metinAlani.setText(""));
        butonPanel.add(temizleButon);
        panel.add(butonPanel, BorderLayout.SOUTH);

        buton.addActionListener(action);

        return panel;
    }

    // Active Directory sekmesi oluşturma metodu
    private static JPanel createActiveDirectoryTab(JButton buton) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        buton.addActionListener(e -> {
            JFrame activeDirectoryFrame = new JFrame("Active Directory Yönetimi");
            activeDirectoryFrame.setSize(800, 600);
            activeDirectoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea adMetinAlani = new JTextArea();
            adMetinAlani.setEditable(false);
            JScrollPane kaydir = new JScrollPane(adMetinAlani);

            JButton getirButon = new JButton("Bilgileri Getir");
            getirButon.addActionListener(ev -> komutCalistir("powershell Get-ADUser -Filter *", adMetinAlani));

            JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            altPanel.add(getirButon);

            activeDirectoryFrame.add(kaydir, BorderLayout.CENTER);
            activeDirectoryFrame.add(altPanel, BorderLayout.SOUTH);

            activeDirectoryFrame.setVisible(true);
        });

        panel.add(buton, BorderLayout.CENTER);
        return panel;
    }

    // Sistem bilgilerini gösteren metot
    private static void sistemBilgileriniGoster(JTextArea metinAlani) {
        Properties sistemOzellikleri = System.getProperties();
        StringBuilder sistemBilgileri = new StringBuilder();
        for (Map.Entry<Object, Object> ozellik : sistemOzellikleri.entrySet()) {
            sistemBilgileri.append(ozellik.getKey()).append(" = ").append(ozellik.getValue()).append("\n");
        }
        metinAlani.setText(sistemBilgileri.toString());
    }

    // Ortam değişkenlerini gösteren metot
    private static void ortamDegiskenleriniGoster(JTextArea metinAlani) {
        Map<String, String> ortamDegiskenleri = System.getenv();
        StringBuilder ortamBilgileri = new StringBuilder();
        for (Map.Entry<String, String> degisken : ortamDegiskenleri.entrySet()) {
            ortamBilgileri.append(degisken.getKey()).append(" = ").append(degisken.getValue()).append("\n");
        }
        metinAlani.setText(ortamBilgileri.toString());
    }

    // Şifreler ve kimlik bilgileri gösteren metot (Placeholder)
    private static void sifreleriGoster(JTextArea metinAlani) {
        metinAlani.setText("Şifreler ve kimlik bilgileri buraya gelecek.");
    }

    // CMD ve Powershell versiyonlarını gösteren metot
    private static void versiyonlariGoster(JTextArea metinAlani) {
        StringBuilder versiyonBilgileri = new StringBuilder();
        try {
            // CMD versiyonu
            Process cmdProcess = Runtime.getRuntime().exec("cmd.exe /c ver");
            BufferedReader cmdReader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            versiyonBilgileri.append("CMD Versiyonu:\n");
            String cmdLine;
            while ((cmdLine = cmdReader.readLine()) != null) {
                versiyonBilgileri.append(cmdLine).append("\n");
            }

            // Powershell versiyonu
            Process psProcess = Runtime.getRuntime().exec("powershell.exe $PSVersionTable.PSVersion");
            BufferedReader psReader = new BufferedReader(new InputStreamReader(psProcess.getInputStream()));
            versiyonBilgileri.append("\nPowershell Versiyonu:\n");
            String psLine;
            while ((psLine = psReader.readLine()) != null) {
                versiyonBilgileri.append(psLine).append("\n");
            }

        } catch (Exception e) {
            versiyonBilgileri.append("Hata oluştu: ").append(e.getMessage());
        }
        metinAlani.setText(versiyonBilgileri.toString());
    }

    // Komut çalıştıran metot
    private static void komutCalistir(String komut, JTextArea metinAlani) {
        try {
            Process process = Runtime.getRuntime().exec(komut);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            metinAlani.setText(output.toString());
        } catch (Exception e) {
            metinAlani.setText("Komut çalıştırılırken bir hata oluştu: " + e.getMessage());
        }
    }
}