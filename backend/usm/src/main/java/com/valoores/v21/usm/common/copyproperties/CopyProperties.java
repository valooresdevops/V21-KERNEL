package com.valoores.v21.usm.common.copyproperties;

import java.lang.reflect.Field;


public class CopyProperties{

	public <T extends Object, Y extends Object> CopyProperties(T from, Y too) {

	    Class<? extends Object> fromClass = from.getClass();
	    Field[] fromFields = fromClass.getDeclaredFields();

	    Class<? extends Object> tooClass = too.getClass();
	    Field[] tooFields = tooClass.getDeclaredFields();

	    if (fromFields != null && tooFields != null) {
	        for (Field tooF : tooFields) {
//	            logger.debug("toofield name #0 and type #1", tooF.getName(), tooF.getType().toString());
	            try {
	                // Check if that fields exists in the other method
	                Field fromF = fromClass.getDeclaredField(tooF.getName());
	                if (fromF.getType().equals(tooF.getType())) {
	                    tooF.set(tooF, fromF);
	                }
	            } catch (SecurityException e) {
	                e.printStackTrace();
	            } catch (NoSuchFieldException e) {
	                e.printStackTrace();
	            } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }

	        }
	    }
}
}
