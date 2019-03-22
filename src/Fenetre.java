import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Fenetre extends JFrame 
	implements ActionListener {
	
	LecteurDico LD;
	JLabel[] noms;
	JButton bouton;
	int nbNoms = 10;
	
	JScrollPane scrollPanel;
	JPanel namesPanel;
	
	private boolean debug = false;
	
	public Fenetre() throws HeadlessException {
		initFenetre();
		lectureDico();
	}

	public Fenetre(GraphicsConfiguration arg0) {
		super(arg0);
		initFenetre();
		lectureDico();
	}

	public Fenetre(String arg0) throws HeadlessException {
		super(arg0);
		initFenetre();
		lectureDico();
	}

	public Fenetre(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		initFenetre();
		lectureDico();
	}
	
	private void initFenetre() {
		//D�finit un titre pour notre fen�tre
	    this.setTitle("G�n�rateur de surnoms de LG");
	    //D�finit sa taille : 300 pixels de large et 300 pixels de haut
	    this.setSize(300, 300);
	    //Indique que la fen�tre ne peut pas �tre redimenssionn�e
	    this.setResizable(false);
	    //Nous demandons maintenant � notre objet de se positionner au centre
	    this.setLocationRelativeTo(null);
	    //Termine le processus lorsqu'on clique sur la croix rouge
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // init des composants affich�s � l'�cran
	    noms = new JLabel[nbNoms];
	    bouton = new JButton("Chargement en cours");
	    bouton.setEnabled(false); // le bouton est d�sactiv� au d�but
	    
	    // init des panneaux de la fen�tre
	    namesPanel = new JPanel();
	    namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.PAGE_AXIS));
	    scrollPanel = new JScrollPane(namesPanel);

	    // ajoute les noms au panneau
	    for(int i = 0 ; i < nbNoms ; i++){
	    	noms[i] = new JLabel();
	    	namesPanel.add(noms[i]);
	    }
	    
	    // branchage du click du bouton
	    bouton.addActionListener(this);
	    
	    // affichage des composants de la fenetre
	    this.getContentPane().setLayout(new BorderLayout());
	    this.add(scrollPanel, BorderLayout.CENTER);
	    this.add(bouton, BorderLayout.SOUTH);
	    
	    //Et enfin, on rends la fen�tre visible        
	    this.setVisible(true);
	}

	private void lectureDico(){

		// on initialise le menu de selection du fichier
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Veuillez selectionner un dictionnaire");
		chooser.setAcceptAllFileFilterUsed(false);
		
		// on utilise le menu pour demander � l'utilisateur de selectionner un dico
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			// chargement du dictionnaire
			LD = new LecteurDico(chooser.getSelectedFile().toString());
			
			// une fois le dictionnaire charg� on active le bouton
			bouton.setText("G�n�rer");
			bouton.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// pour chaque libell� affich�
	    for(JLabel nom : noms){
			// on efface le nom actuellement affich�
			nom.setText("");
			
			// on g�n�re un nouveau nom
			String text = LD.combinaisonAleatoire();
			
			if(debug) {
				System.out.println(" g�n�r� : " + text);
			}
		
			//insertion du nouveau nom dans le panneau
			nom.setText(text);
	    }
	}
}
