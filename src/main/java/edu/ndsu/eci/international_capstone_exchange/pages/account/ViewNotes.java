package edu.ndsu.eci.international_capstone_exchange.pages.account;

import edu.ndsu.eci.international_capstone_exchange.persist.PairingNotes;
import org.apache.cayenne.ObjectContext;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.eci.international_capstone_exchange.auth.ILACRealm;
import edu.ndsu.eci.international_capstone_exchange.persist.Pairing;
import edu.ndsu.eci.international_capstone_exchange.services.UserInfo;
import edu.ndsu.eci.international_capstone_exchange.persist.User;

import java.util.ArrayList;
import java.util.List;

public class ViewNotes {
    @Property
    private Pairing pairing;

    @Inject
    private UserInfo userInfo;

    @Property
    private User user;

    @Property
    private List<PairingNotes> pairingNotes;

    @Property
    private PairingNotes row;

    /** cayenne context */
    @Inject
    private ObjectContext context;

    @RequiresPermissions(ILACRealm.PAIRING_VIEW_INSTANCE)
    public void onActivate(Pairing pairing) {
        this.pairing = pairing;

        user = (User) context.localObject(userInfo.getUser().getObjectId(), null);
    }

    public Pairing onPassivate() {
        return pairing;
    }

    public void setupRender() {
        if(userInfo.isAdmin())
        {
            pairingNotes = pairing.getNotes();
        }
        else
        {
            pairingNotes = new ArrayList<>();
            List<PairingNotes> pn;
            pn = pairing.getNotes();
            for (PairingNotes pairingNotestemp : pn) {
                if (!pairingNotestemp.getAdminOnly()) {
                    pairingNotes.add(pairingNotestemp);
                }
            }
        }
    }

    //allow logged in user to delete their own notes
    public boolean checkUser() {
        if(row.getUser() == (User) context.localObject(userInfo.getUser().getObjectId(), null))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public void onDelete(PairingNotes pairingNote) {
        context.deleteObject(pairingNote);
        context.commitChanges();
    }
}