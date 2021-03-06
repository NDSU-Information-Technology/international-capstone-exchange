package edu.ndsu.eci.international_capstone_exchange.persist.auto;

import java.util.Date;
import java.util.List;

import org.apache.cayenne.CayenneDataObject;

import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.util.Status;

/**
 * Class _ProposalType was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _ProposalType extends CayenneDataObject {

    public static final String CREATED_PROPERTY = "created";
    public static final String DESCRIPTION_PROPERTY = "description";
    public static final String NAME_PROPERTY = "name";
    public static final String STATUS_PROPERTY = "status";
    public static final String PROPOSAL_PROPERTY = "proposal";

    public static final String PK_PK_COLUMN = "pk";

    public void setCreated(Date created) {
        writeProperty("created", created);
    }
    public Date getCreated() {
        return (Date)readProperty("created");
    }

    public void setDescription(String description) {
        writeProperty("description", description);
    }
    public String getDescription() {
        return (String)readProperty("description");
    }

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
    }

    public void setStatus(Status status) {
        writeProperty("status", status);
    }
    public Status getStatus() {
        return (Status)readProperty("status");
    }

    public void addToProposal(Proposal obj) {
        addToManyTarget("proposal", obj, true);
    }
    public void removeFromProposal(Proposal obj) {
        removeToManyTarget("proposal", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Proposal> getProposal() {
        return (List<Proposal>)readProperty("proposal");
    }


}
