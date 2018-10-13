package edu.ndsu.eci.international_capstone_exchange.pages.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.tapestry5cayenne.annotations.CommitAfter;
import edu.ndsu.eci.international_capstone_exchange.auth.InstanceAccessMethod;
import edu.ndsu.eci.international_capstone_exchange.persist.*;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.SimpleEmail;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.googlecode.tapestry5cayenne.PersistentEntitySelectModel;
import com.googlecode.tapestry5cayenne.annotations.Cayenne;

import edu.ndsu.eci.international_capstone_exchange.auth.ILACRealm;
import edu.ndsu.eci.international_capstone_exchange.services.HtmlCleaner;
import edu.ndsu.eci.international_capstone_exchange.services.UserInfo;
import edu.ndsu.eci.international_capstone_exchange.services.VelocityEmailService;
import edu.ndsu.eci.international_capstone_exchange.util.ProposalStatus;
import edu.ndsu.eci.international_capstone_exchange.util.Status;

public class AddNotes {

    /** user info service */
    @Inject
    private UserInfo userInfo;

    /** cayenne context */
    @Inject
    private ObjectContext context;

    /** page to go back to */
    @InjectPage
    private ViewNotes viewNotes;

    /** alerts */
    @Inject
    private AlertManager alerts;

    /** form object */
    @Property
    private PairingNotes pairingNotes;


    /** form */
    @Component
    private BeanEditForm form;

    @Property
    private Pairing pairing;

    /** html cleaner */
    @Inject
    private HtmlCleaner cleaner;

    public Object onPassivate() {
        return pairing;
    }

    @RequiresPermissions(ILACRealm.PAIRING_VIEW_INSTANCE)
    public void onActivate(Pairing pairing) {
        this.pairing = pairing;
        pairingNotes = new PairingNotes();
    }

    /**
     * Validate the form
     */
    public void onValidateFromForm() {
        if (StringUtils.isBlank(pairingNotes.getNote())) {
            form.recordError("Must enter some notes");
            context.rollbackChanges();
        }
    }

    public boolean checkAdmin()
    {
        return userInfo.isAdmin();
    }

    /**
     * On form success submission
     * @return return page
     * @throws Exception
     * @throws ParseErrorException
     * @throws ResourceNotFoundException
     */
    @CommitAfter
    public Object onSuccessFromForm() throws ResourceNotFoundException, ParseErrorException, Exception {

        pairingNotes.setPairing(pairing);
        pairingNotes.setUser((User) context.localObject(userInfo.getUser().getObjectId(), null));
        pairingNotes.setTmstamp(new Date());
        pairingNotes.setNote(cleaner.cleanCapstone(pairingNotes.getNote()));
        if(!checkAdmin())
        {
            pairingNotes.setAdminOnly(false);
        }
        else
        {
            pairingNotes.setAdminOnly(pairingNotes.getAdminOnly());
        }

        alerts.success("Notes submitted");

        viewNotes.onActivate(pairingNotes.getPairing());
        return viewNotes;
    }


}