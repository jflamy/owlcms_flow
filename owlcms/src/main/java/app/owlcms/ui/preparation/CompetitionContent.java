/***
 * Copyright (c) 2009-2019 Jean-François Lamy
 * 
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)  
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.ui.preparation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

import org.slf4j.LoggerFactory;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

import app.owlcms.components.fields.LocalDateField;
import app.owlcms.data.competition.Competition;
import app.owlcms.data.competition.CompetitionRepository;
import app.owlcms.init.OwlcmsSession;
import app.owlcms.ui.crudui.OwlcmsCrudFormFactory;
import app.owlcms.ui.shared.AppLayoutAware;
import app.owlcms.ui.shared.ContentWrapping;
import app.owlcms.ui.shared.OwlcmsRouterLayout;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * Class PreparationNavigationContent.
 */
@SuppressWarnings("serial")
@Route(value = "preparation/competition", layout = CompetitionLayout.class)
public class CompetitionContent extends VerticalLayout
		implements ContentWrapping, CrudLayout, AppLayoutAware {

	Logger logger = (Logger) LoggerFactory.getLogger(CompetitionContent.class);
	private OwlcmsRouterLayout routerLayout;
	public void initLoggers() {
		logger.setLevel(Level.INFO);
	}

	/**
	 * Instantiates a new preparation navigation content.
	 */
	public CompetitionContent() {
		initLoggers();
		DefaultCrudFormFactory<Competition> factory = createFormFactory();
		Component form = factory.buildNewForm(CrudOperation.UPDATE, Competition.getCurrent(), false, null, event -> {
            try {
            	CompetitionRepository.save(Competition.getCurrent());
            } catch (IllegalArgumentException ignore) {
            } catch (Exception e2) {
                throw e2;
            }
        });	
		fillH(form, this);	
	}


	
	/**
	 * Define the form used to edit a given athlete.
	 * 
	 * @return the form factory that will create the actual form on demand
	 */
	protected DefaultCrudFormFactory<Competition>  createFormFactory() {
		DefaultCrudFormFactory<Competition>  athleteEditingFormFactory = createCompetitionEditingFormFactory();
		createFormLayout(athleteEditingFormFactory);
		return athleteEditingFormFactory;
	}

	/**
	 * The content and ordering of the editing form
	 * 
	 * @param crudFormFactory the factory that will create the form using this information
	 */
	private void createFormLayout(DefaultCrudFormFactory<Competition> crudFormFactory) {
		crudFormFactory.setVisibleProperties(
			"competitionName",
			"competitionDate",
			"competitionOrganizer",
			"competitionSite",
			"competitionCity",
			"federation",
			"federationAddress",
			"federationEMail",
			"federationWebSite",
			"defaultLocale",
//			"protocolFileName",
//			"finalPackageTemplateFileName",
			"enforce20kgRule",
			"masters",
			"useBirthYear"
			);
		ItemLabelGenerator<Locale> nameGenerator = (locale) -> locale.getDisplayName(Locale.US);
		crudFormFactory.setFieldProvider("defaultLocale",
            new ComboBoxProvider<Locale>("Locale", Arrays.asList(Locale.ENGLISH,Locale.FRENCH), new TextRenderer<>(nameGenerator), 
            		nameGenerator));
		crudFormFactory.setFieldType("competitionDate", LocalDateField.class);
	}
	
	/**
	 * Create the actual form generator with all the conversions and validations required
	 * 
	 * @return the actual factory with field binding and validations
	 */
	private DefaultCrudFormFactory<Competition> createCompetitionEditingFormFactory() {
		return new OwlcmsCrudFormFactory<Competition>(Competition.class) {
			/** 
			 * Override this method if you need to add custom validations
			 * 
			 * @see org.vaadin.crudui.form.AbstractAutoGeneratedCrudFormFactory#bindField(com.vaadin.flow.component.HasValue, java.lang.String, java.lang.Class)
			 */
			@SuppressWarnings({ "rawtypes" })
			@Override
			protected void bindField(HasValue field, String property, Class<?> propertyType) {
				if ("competitionDate".equals(property)) {
					LocalDateField f = ((LocalDateField)field);
					Validator<LocalDate> v = f.formatValidation(OwlcmsSession.getLocale());
					binder.forField(f).withValidator(v).bind(property);		
				} else {
					super.bindField(field, property, propertyType);
				}
			}
		};
	}

	public Competition update(Competition domainObjectToUpdate) {
			Competition ndo = CompetitionRepository.save(domainObjectToUpdate);
			return ndo;
	}

	@Override
	public void setMainComponent(Component component) {
		this.removeAll();
		this.add(component);
	}

	@Override
	public void addFilterComponent(Component component) {
	}

	@Override
	public void addToolbarComponent(Component component) {
	}

	@Override
	public void showForm(CrudOperation operation, Component form, String caption) {
		this.removeAll();
		this.add(form);
	}

	@Override
	public void hideForm() {
	}

	@Override
	public void showDialog(String caption, Component form) {
	}

	@Override
	public OwlcmsRouterLayout getRouterLayout() {
		return routerLayout;
	}

	@Override
	public void setRouterLayout(OwlcmsRouterLayout routerLayout) {
		this.routerLayout = routerLayout;
	}
}
