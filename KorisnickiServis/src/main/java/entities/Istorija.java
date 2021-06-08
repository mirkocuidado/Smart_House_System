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
@Table(name = "istorija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Istorija.findAll", query = "SELECT i FROM Istorija i"),
    @NamedQuery(name = "Istorija.findById", query = "SELECT i FROM Istorija i WHERE i.id = :id"),
    @NamedQuery(name = "Istorija.findByNaziv", query = "SELECT i FROM Istorija i WHERE i.naziv = :naziv"),
    @NamedQuery(name = "Istorija.findByAutor", query = "SELECT i FROM Istorija i WHERE i.autor = :autor"),
    @NamedQuery(name = "Istorija.findByUsername", query = "SELECT i FROM Istorija i WHERE i.username = :username"),
    @NamedQuery(name = "Istorija.findByDate", query = "SELECT i FROM Istorija i WHERE i.date = :date")})
public class Istorija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Size(max = 45)
    @Column(name = "autor")
    private String autor;
    @Size(max = 45)
    @Column(name = "username")
    private String username;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Istorija() {
    }

    public Istorija(Integer id) {
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        if (!(object instanceof Istorija)) {
            return false;
        }
        Istorija other = (Istorija) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Istorija[ id=" + id + " ]";
    }
    
}
