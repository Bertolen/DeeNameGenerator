import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Fenetre extends JFrame 
	implements ActionListener {
	
	LecteurDico LD;
	JLabel nom;
	JButton bouton;
	
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
		//Définit un titre pour notre fenêtre
	    this.setTitle("Comparateur de fichiers de masse");
	    //Définit sa taille : 600 pixels de large et 400 pixels de haut
	    this.setSize(600, 400);
	    //Nous demandons maintenant à notre objet de se positionner au centre
	    this.setLocationRelativeTo(null);
	    //Termine le processus lorsqu'on clique sur la croix rouge
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // init des composants affichés à l'écran
	    nom = new JLabel("Hello World!");
	    bouton = new JButton("Generate");
	    
	    // branchage du click du bouton
	    bouton.addActionListener(this);
	    
	    // affichage des composants de la fenetre
	    this.getContentPane().setLayout(new BorderLayout());
	    this.add(nom, BorderLayout.CENTER);
	    this.add(bouton, BorderLayout.SOUTH);
	    
	    //Et enfin, on rends la fenêtre visible        
	    this.setVisible(true);
	}

	private void lectureDico(){

		// on initialise le menu de selection du fichier
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Veuillez selectionner un dictionnaire");
		chooser.setAcceptAllFileFilterUsed(false);
		
		// on utilise le menu pour demander à l'utilisateur de selectionner un dico
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			LD = new LecteurDico(chooser.getSelectedFile().toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// on efface le texte actuellement affiché
		nom.setText("");
		
		// init du générateur aléatoire
		Random r = new Random();
		
		// on génère un nouveau nom
		String text = new String();
		text = text.concat(LD.noms().get( r.nextInt( LD.noms().size())));
		text = text.concat(" ");
		text = text.concat(LD.adjectifs().get( r.nextInt( LD.adjectifs().size())));
		
		if(debug) {
			System.out.println("Nom généré : " + text);
		}
		
		// insertion du nouveau nom dans le texte affichable
		nom.setText(text);
	}
}
