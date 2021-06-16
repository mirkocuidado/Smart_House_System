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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 38164
 */
@Entity
@Table(name = "alarm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a"),
    @NamedQuery(name = "Alarm.findById", query = "SELECT a FROM Alarm a WHERE a.id = :id"),
    @NamedQuery(name = "Alarm.findByVreme", query = "SELECT a FROM Alarm a WHERE a.vreme = :vreme"),
    @NamedQuery(name = "Alarm.findByZvuk", query = "SELECT a FROM Alarm a WHERE a.zvuk = :zvuk"),
    @NamedQuery(name = "Alarm.findByPeriodican", query = "SELECT a FROM Alarm a WHERE a.periodican = :periodican"),
    @NamedQuery(name = "Alarm.findByPerioda", query = "SELECT a FROM Alarm a WHERE a.perioda = :perioda")})
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vreme;
    @Column(name = "zvuk")
    private Integer zvuk;
    @Column(name = "periodican")
    private Integer periodican;
    @Column(name = "perioda")
    private Integer perioda;

    public Alarm() {
    }

    public Alarm(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public Integer getZvuk() {
        return zvuk;
    }

    public void setZvuk(Integer zvuk) {
        this.zvuk = zvuk;
    }

    public Integer getPeriodican() {
        return periodican;
    }

    public void setPeriodican(Integer periodican) {
        this.periodican = periodican;
    }

    public Integer getPerioda() {
        return perioda;
    }

    public void setPerioda(Integer perioda) {
        this.perioda = perioda;
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
        if (!(object instanceof Alarm)) {
            return false;
        }
        Alarm other = (Alarm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Alarm[ id=" + id + " ]";
    }
    
}
