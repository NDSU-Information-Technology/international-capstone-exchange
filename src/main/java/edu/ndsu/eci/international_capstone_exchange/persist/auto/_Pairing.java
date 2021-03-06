package edu.ndsu.eci.international_capstone_exchange.persist.auto;

import java.util.Date;
import java.util.List;

import org.apache.cayenne.CayenneDataObject;

import edu.ndsu.eci.international_capstone_exchange.persist.PairingNotes;
import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.persist.User;

/**
 * Class _Pairing was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Pairing extends CayenneDataObject {

    public static final String NAME_PROPERTY = "name";
    public static final String TMSTAMP_PROPERTY = "tmstamp";
    public static final String ADMIN_PROPERTY = "admin";
    public static final String NOTES_PROPERTY = "notes";
    public static final String PROPOSALS_PROPERTY = "proposals";

    public static final String PK_PK_COLUMN = "pk";

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
    }

    public void setTmstamp(Date tmstamp) {
        writeProperty("tmstamp", tmstamp);
    }
    public Date getTmstamp() {
        return (Date)readProperty("tmstamp");
    }

    public void setAdmin(User admin) {
        setToOneTarget("admin", admin, true);
    }

    public User getAdmin() {
        return (User)readProperty("admin");
    }


    public void addToNotes(PairingNotes obj) {
        addToManyTarget("notes", obj, true);
    }
    public void removeFromNotes(PairingNotes obj) {
        removeToManyTarget("notes", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<PairingNotes> getNotes() {
        return (List<PairingNotes>)readProperty("notes");
    }


    public void addToProposals(Proposal obj) {
        addToManyTarget("proposals", obj, true);
    }
    public void removeFromProposals(Proposal obj) {
        removeToManyTarget("proposals", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Proposal> getProposals() {
        return (List<Proposal>)readProperty("proposals");
    }


}
