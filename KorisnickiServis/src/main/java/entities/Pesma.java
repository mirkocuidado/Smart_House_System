/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 38164
 */
@Entity
@Table(name = "pesma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pesma.findAll", query = "SELECT p FROM Pesma p"),
    @NamedQuery(name = "Pesma.findByIdpesma", query = "SELECT p FROM Pesma p WHERE p.idpesma = :idpesma"),
    @NamedQuery(name = "Pesma.findByUrl", query = "SELECT p FROM Pesma p WHERE p.url = :url"),
    @NamedQuery(name = "Pesma.findByAutor", query = "SELECT p FROM Pesma p WHERE p.autor = :autor"),
    @NamedQuery(name = "Pesma.findByNaziv", query = "SELECT p FROM Pesma p WHERE p.naziv = :naziv"),
    @NamedQuery(name = "Pesma.findByUsername", query = "SELECT p FROM Pesma p WHERE p.username = :username")})
public class Pesma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpesma")
    private Integer idpesma;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "url")
    private String url;
    @Size(max = 45)
    @Column(name = "autor")
    private String autor;
    @Size(max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Size(max = 45)
    @Column(name = "username")
    private String username;

    public Pesma() {
    }

    public Pesma(Integer idpesma) {
        this.idpesma = idpesma;
    }

    public Pesma(Integer idpesma, String url) {
        this.idpesma = idpesma;
        this.url = url;
    }

    public Integer getIdpesma() {
        return idpesma;
    }

    public void setIdpesma(Integer idpesma) {
        this.idpesma = idpesma;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpesma != null ? idpesma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pesma)) {
            return false;
        }
        Pesma other = (Pesma) object;
        if ((this.idpesma == null && other.idpesma != null) || (this.idpesma != null && !this.idpesma.equals(other.idpesma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Pesma[ idpesma=" + idpesma + " ]";
    }
    
}
