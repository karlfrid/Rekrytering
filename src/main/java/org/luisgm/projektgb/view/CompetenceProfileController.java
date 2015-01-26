package org.luisgm.projektgb.view;

import org.luisgm.projektgb.model.CompetenceProfile;
import org.luisgm.projektgb.view.util.JsfUtil;
import org.luisgm.projektgb.view.util.JsfUtil.PersistAction;
import org.luisgm.projektgb.controll.CompetenceProfileFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("competenceProfileController")
@SessionScoped
public class CompetenceProfileController implements Serializable {

    @EJB
    private org.luisgm.projektgb.controll.CompetenceProfileFacade ejbFacade;
    private List<CompetenceProfile> items = null;
    private CompetenceProfile selected;

    public CompetenceProfileController() {
    }

    public CompetenceProfile getSelected() {
        return selected;
    }

    public void setSelected(CompetenceProfile selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CompetenceProfileFacade getFacade() {
        return ejbFacade;
    }

    public CompetenceProfile prepareCreate() {
        selected = new CompetenceProfile();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("util/Bundle").getString("CompetenceProfileCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("util/Bundle").getString("CompetenceProfileUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("util/Bundle").getString("CompetenceProfileDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CompetenceProfile> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("util/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("util/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public CompetenceProfile getCompetenceProfile(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CompetenceProfile> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CompetenceProfile> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CompetenceProfile.class)
    public static class CompetenceProfileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CompetenceProfileController controller = (CompetenceProfileController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "competenceProfileController");
            return controller.getCompetenceProfile(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CompetenceProfile) {
                CompetenceProfile o = (CompetenceProfile) object;
                return getStringKey(o.getCompetenceProfileId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CompetenceProfile.class.getName()});
                return null;
            }
        }

    }

}
