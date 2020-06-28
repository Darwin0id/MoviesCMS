/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.darwin.component;

import hr.darwin.dal.RepositoryFactory;
import hr.algebra.utils.MessageUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import hr.darwin.model.Movie;
import hr.darwin.handler.movie.IMovie;
import hr.darwin.model.MovieCollection;
import hr.darwin.parser.MovieParser;
/**
 *
 * @author darwin
 */
public class GetMoviesPanel extends javax.swing.JPanel {

    private DefaultListModel<Movie> movieModel;
    private static final String FILENAME = "parsedMovies.xml";
    private static final String IMAGEFILE = "src\\assets\\movie";
    
    private IMovie movieHandler;
    
    /**
     * Creates new form GetMoviesPanel
     */
    public GetMoviesPanel() {
        initComponents();
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lsMovie = new javax.swing.JList<>();
        btnXML = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        btnDelete1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(900, 773));

        jScrollPane1.setViewportView(lsMovie);

        btnXML.setText("Download XML entity");
        btnXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXMLActionPerformed(evt);
            }
        });

        btnUpload.setText("Upload movies");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        btnDelete1.setText("Delete movies");
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXML, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpload, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addComponent(btnDelete1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXML, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        try {
            List<Movie> movies = MovieParser.parse();
            if (0 != movies.size()) {
                movieHandler.createMovies(movies);
                loadModel();   
            }
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(GetMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GetMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    private void btnXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXMLActionPerformed
        try {
            save(new MovieCollection(movieHandler.selectMovies()), FILENAME);
        } catch (Exception e) {
            Logger.getLogger(GetMoviesPanel.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_btnXMLActionPerformed

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        try {
            movieHandler.deleteMovies();
            loadModel();
            Files.walk(Paths.get(IMAGEFILE))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (Exception ex) {
            Logger.getLogger(GetMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDelete1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete1;
    private javax.swing.JButton btnUpload;
    private javax.swing.JButton btnXML;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<Movie> lsMovie;
    // End of variables declaration//GEN-END:variables

    private void init() {
        try {
            movieHandler = RepositoryFactory.getMovieHandler();
            movieModel = new DefaultListModel();
            loadModel();
        } catch (Exception e) {
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }

    private void loadModel() {
        try {
            List<Movie> movies = movieHandler.selectMovies();
            movieModel.clear();
            movies.forEach(movie -> movieModel.addElement(movie));
            lsMovie.setModel(movieModel);
        } catch (Exception ex) {
            Logger.getLogger(GetMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void save(MovieCollection movieCollection, String file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(MovieCollection.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //marshaller.marshal(movieCollection, System.out);
        marshaller.marshal(movieCollection, new File(file));
    }
}