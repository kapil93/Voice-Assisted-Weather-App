package kapil.voiceassistedweatherapp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Custom scope intended to match the scope of the activity.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
@interface ActivityScope {
}
