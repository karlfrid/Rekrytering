package org.luisgm.projektgb.view;

import org.luisgm.projektgb.model.Availability;
import org.luisgm.projektgb.view.util.JsfUtil;
import org.luisgm.projektgb.view.util.JsfUtil.PersistAction;
import org.luisgm.projektgb.controll.AvailabilityFacade;

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

@Named("availabilityController")
@SessionScoped
public class AvailabilityController implements Serializable {

    @EJB
    private org.luisgm.projektgb.controll.AvailabilityFacade ejbFacade;
    private List<Availability> items = null;
    private Availability selected;

    public AvailabilityController() {
    }

    public Availability getSelected() {
        return selected;
    }

    public void setSelected(Availability selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AvailabilityFacade getFacade() {
        return ejbFacade;
    }

    public Availability prepareCreate() {
        selected = new Availability();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("util/Bundle").getString("AvailabilityCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("util/Bundle").getString("AvailabilityUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("util/Bundle").getString("AvailabilityDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Availability> getItems() {
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

    public Availability getAvailability(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Availability> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Availability> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Availability.class)
    public static class AvailabilityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvailabilityController controller = (AvailabilityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "availabilityController");
            return controller.getAvailability(getKey(value));
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
            if (object instanceof Availability) {
                Availability o = (Availability) object;
                return getStringKey(o.getAvailabilityId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Availability.class.getName()});
                return null;
            }
        }

    }

}
