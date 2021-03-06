package org.luisgm.projektgb.view;

import org.luisgm.projektgb.model.Competence;
import org.luisgm.projektgb.view.util.JsfUtil;
import org.luisgm.projektgb.view.util.JsfUtil.PersistAction;
import org.luisgm.projektgb.controll.CompetenceFacade;

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

@Named("competenceController")
@SessionScoped
public class CompetenceController implements Serializable {

    @EJB
    private org.luisgm.projektgb.controll.CompetenceFacade ejbFacade;
    private List<Competence> items = null;
    private Competence selected;

    public CompetenceController() {
    }

    public Competence getSelected() {
        return selected;
    }

    public void setSelected(Competence selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CompetenceFacade getFacade() {
        return ejbFacade;
    }

    public Competence prepareCreate() {
        selected = new Competence();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("util/Bundle").getString("CompetenceCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("util/Bundle").getString("CompetenceUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("util/Bundle").getString("CompetenceDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Competence> getItems() {
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

    public Competence getCompetence(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Competence> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Competence> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Competence.class)
    public static class CompetenceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CompetenceController controller = (CompetenceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "competenceController");
            return controller.getCompetence(getKey(value));
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
            if (object instanceof Competence) {
                Competence o = (Competence) object;
                return getStringKey(o.getCompetenceId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Competence.class.getName()});
                return null;
            }
        }

    }

}
