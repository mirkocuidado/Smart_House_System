/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 38164
 */
@Entity
@Table(name = "obaveza")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obaveza.findAll", query = "SELECT o FROM Obaveza o"),
    @NamedQuery(name = "Obaveza.findById", query = "SELECT o FROM Obaveza o WHERE o.id = :id"),
    @NamedQuery(name = "Obaveza.findByNaziv", query = "SELECT o FROM Obaveza o WHERE o.naziv = :naziv"),
    @NamedQuery(name = "Obaveza.findByKada", query = "SELECT o FROM Obaveza o WHERE o.kada = :kada"),
    @NamedQuery(name = "Obaveza.findByLokacija", query = "SELECT o FROM Obaveza o WHERE o.lokacija = :lokacija"),
    @NamedQuery(name = "Obaveza.findByUsername", query = "SELECT o FROM Obaveza o WHERE o.username = :username"),
    @NamedQuery(name = "Obaveza.findByTrajanje", query = "SELECT o FROM Obaveza o WHERE o.trajanje = :trajanje")})
public class Obaveza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Column(name = "kada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date kada;
    @Size(max = 45)
    @Column(name = "lokacija")
    private String lokacija;
    @Size(max = 45)
    @Column(name = "username")
    private String username;
    @Column(name = "trajanje")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trajanje;

    public Obaveza() {
    }

    public Obaveza(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Date getKada() {
        return kada;
    }

    public void setKada(Date kada) {
        this.kada = kada;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(Date trajanje) {
        this.trajanje = trajanje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Obaveza)) {
            return false;
        }
        Obaveza other = (Obaveza) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Obaveza[ id=" + id + " ]";
    }
    
}
