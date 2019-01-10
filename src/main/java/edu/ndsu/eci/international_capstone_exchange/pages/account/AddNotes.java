// Copyright 2018 North Dakota State University
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package edu.ndsu.eci.international_capstone_exchange.pages.account;

import java.util.Date;

import com.googlecode.tapestry5cayenne.annotations.CommitAfter;
import org.apache.cayenne.ObjectContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import edu.ndsu.eci.international_capstone_exchange.auth.ILACRealm;
import edu.ndsu.eci.international_capstone_exchange.persist.Pairing;
import edu.ndsu.eci.international_capstone_exchange.persist.PairingNotes;
import edu.ndsu.eci.international_capstone_exchange.persist.User;
import edu.ndsu.eci.international_capstone_exchange.services.HtmlCleaner;
import edu.ndsu.eci.international_capstone_exchange.services.UserInfo;

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