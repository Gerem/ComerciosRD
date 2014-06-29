package com.comerciosrd.utils;

import android.content.Context;
import android.content.Intent;


public class CommonUtilities {
	public static final String APP_NAME						= "Comercios RD";
	public static final String API_URL						= "http://aleyworld.com/ApiServer/api/";
	public static final String API_LOCATION_MODULE 			= "localidad";
	public static final String API_CATEGORY_MODULE 			= "categories";
	public static final String API_CLIENT_MODULE 			= "cliente";
	public static final String API_SEND_MAIL_MODULE			= "sendMail";
	public static final String API_REGISTER_GCM 			= "registerGCM";
	public static final String NEAREST_LOCATION_MESSAGE		= "El mas cercano a ti.";
	public static final String API_CLIENT_LOGO_PATH 		= "http://aleyworld.com/ApiServer/uploads/";
	
	public static final String CLIENT_NAME_FIELD			= "nombreCliente";
	public static final String CATEGORY_NAME_FIELD			= "nombreCategoria";
	public static final String EMAIL_FIELD					= "email";
	public static final String LATITUDE_FIELD				= "latitud";
	public static final String LONGITUDE_FIELD				= "longitud";
	public static final String PHONE_FIELD					= "telefono";
	public static final String GCM_REGID_FIELD				= "gcm_regid";
	public static final String ADDRESS_FIELD				= "direccion";
	public static final String DESCRIPTION_FIELD			= "descripcion";
	
	public static final String MAIN_HEADER_COLOR			= "#1D2227";
	
	public static final String ADMOB_PUBLISHER_ID			= "a152d23767dbc27";
	
	public static final String CATEGORY_VIEW_NAME 			= "CATEGORIAS";
	public static final String OLD_CATEGORY_VIEW_NAME 		= "Negocios";
	
	public static final String EMAIL_SUBJECT				= "Solicitud de Sucursal";
	public static final String ADMIN_EMAIL					= "nelson.molina.ca@gmail.com";
	
	public static final String SENDER_ID 					= "41301393487";
	
	/**
     * Tag used on log messages.
     */
	public static final String TAG = "ComerciosRD GCM";
 
    public static final String DISPLAY_MESSAGE_ACTION ="com.comerciosrd.map.DISPLAY_MESSAGE";
 
    public static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
     
}

