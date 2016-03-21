package com.example.jakub.snake2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Jakub on 2015-08-13.
 */
class ResourceManager
{
    // singleton
    private final static ResourceManager m_instance = new ResourceManager();
    private static final float LEFT_VOLUME = 1;
    private static final float RIGHT_VOLUME = 1;
    private static final int STREAM_PRIORITY = 1;
    private static final int LOOP_MODE = 0;
    private static final float PLAYBACK_RATE = 1;

    private Context m_applicationContext;
    @Nullable
    private SoundPool m_soundPool;
    @Nullable
    private Map<String, Integer> m_audioResources;
    @Nullable
    private HashMap<String, AnimationResource> m_animatedResources;
    @Nullable
    private HashMap<String, Bitmap> m_gfxResources;

    public static ResourceManager getInstance()
    {
        return m_instance;
    }

    public void init(Context context)
    {
        m_applicationContext = context;
        m_animatedResources = new HashMap<>();
        m_audioResources = new HashMap<>();
        m_gfxResources = new HashMap<>();
        m_soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        loadAnimatedResources();
        loadGfxResources();
        loadAudioResources();
    }

    public Bitmap getImage(String name)
    {
        Bitmap image = null;

        if (null != m_gfxResources)
        {
            image = m_gfxResources.get(name);
        }

        return image;
    }

    public AnimationResource getAnimationResource(String resourceName)
    {
        AnimationResource animationResource = null;

        if (null != m_animatedResources)
        {
            animationResource = m_animatedResources.get(resourceName);
        }
        else
        {
            System.out.println("ERROR: ResourceManager::getAnimationResource()");
        }

        return animationResource;
    }

    public void playAudioSample(String sampleName)
    {
        if ((null != m_audioResources) && (null != m_soundPool))
        {
            int sampleToPlay = m_audioResources.get(sampleName);
            m_soundPool.play(sampleToPlay, LEFT_VOLUME, RIGHT_VOLUME, STREAM_PRIORITY, LOOP_MODE, PLAYBACK_RATE);
        }
    }

    public Context getApplicationContext()
    {
        return m_applicationContext;
    }

    public SharedPreferences getSharedPreferences()
    {
        ResourceManager resourceManager = ResourceManager.getInstance();
        return PreferenceManager.getDefaultSharedPreferences(resourceManager.getApplicationContext());
    }

    public int getPreferencesSize()
    {
        SharedPreferences sharedPreferences = ResourceManager.getInstance().getSharedPreferences();
        Map <String, ?> allEntries = sharedPreferences.getAll();

        return allEntries.size();
    }

    private ResourceManager() {}

    private NodeList parseResourcesList(InputStream inputStream)
    {
        NodeList resourcesList = null;

        if (null != m_applicationContext)
        {
            try
            {
                //get the resources list from XML
                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document resourcesXML = docBuilder.parse(inputStream, null);
                resourcesList = resourcesXML.getElementsByTagName("resource");
            }
            catch (Throwable t)
            {
                System.out.println("ERROR: parseResourcesList() exception");
            }
        }

        return resourcesList;
    }

    private void loadGfxResources()
    {
        if ((null != m_applicationContext) && (null != m_gfxResources))
        {
            InputStream inputStream = m_applicationContext.getResources().openRawResource(R.raw.gfx_resources_list);
            NodeList resourcesList = parseResourcesList(inputStream);
            Node nodeItem;
            Bitmap image;
            String name;

            for (short i = 0; i < resourcesList.getLength(); i++)
            {
                nodeItem = resourcesList.item(i);
                org.w3c.dom.Element element = (org.w3c.dom.Element) nodeItem;
                image = retrieveImage(element);
                name = retrieveName(element);

                m_gfxResources.put(name, image);
            }
        }
        else
        {
            System.out.println("ERROR: loadGfxResources::loadAnimatedResources()");
        }
    }

    private void loadAnimatedResources()
    {
        if ((null != m_applicationContext) && (null != m_animatedResources))
        {
            InputStream inputStream = m_applicationContext.getResources().openRawResource(R.raw.animated_resources_list_base);
            NodeList resourcesList = parseResourcesList(inputStream);
            int framesNumber, sameFrameThreshold, height, width;
            Node nodeItem;
            Bitmap image;
            String name;

            for (short i = 0; i < resourcesList.getLength(); i++)
            {
                nodeItem = resourcesList.item(i);
                org.w3c.dom.Element element = (org.w3c.dom.Element) nodeItem;
                image = retrieveImage(element);
                name = retrieveName(element);
                framesNumber = retrieveFramesNumber(element);
                sameFrameThreshold = retrieveFrameThreshold(element);
                height = retrieveHeight(element);
                width = retrieveWidth(element);

                AnimationResource animationResource = new AnimationResource(image, name, framesNumber, sameFrameThreshold, height, width);
                animationResource.init();
                m_animatedResources.put(name, animationResource);
            }
        }
        else
        {
            System.out.println("ERROR: ResourceManager::loadAnimatedResources()");
        }
    }

    private void loadAudioResources()
    {
        if ((null != m_applicationContext) && (null != m_soundPool) && (null != m_audioResources))
        {
            InputStream inputStream = m_applicationContext.getResources().openRawResource(R.raw.audio_resources_list_base);
            NodeList resourcesList = parseResourcesList(inputStream);
            Node nodeItem;
            String resourceName;
            Resources resources;
            int resourceID;
            int sampleID;

            for (short i = 0; i < resourcesList.getLength(); i++)
            {
                nodeItem = resourcesList.item(i);
                org.w3c.dom.Element element = (org.w3c.dom.Element)nodeItem;
                resourceName = retrieveName(element);
                resources = m_applicationContext.getResources();
                resourceID = resources.getIdentifier(resourceName, "raw", m_applicationContext.getPackageName());
                sampleID = m_soundPool.load(m_applicationContext, resourceID, 1);
                m_audioResources.put(resourceName, sampleID);
            }
        }
        else
        {
            System.out.println("ERROR: ResourceManager::loadAudioResources()");
        }
    }

    private Bitmap retrieveImage(org.w3c.dom.Element element)
    {
        String resourceName = retrieveName(element);
        Resources resources = m_applicationContext.getResources();
        int resourceID = resources.getIdentifier(resourceName, "drawable", m_applicationContext.getPackageName());

        return BitmapFactory.decodeResource(resources, resourceID);
    }

    private String retrieveName(org.w3c.dom.Element element)
    {
        return element.getAttribute("name");
    }

    private int retrieveFramesNumber(org.w3c.dom.Element element)
    {
        String framesNumber = element.getAttribute("framesNumber");
        return Integer.parseInt(framesNumber);
    }

    private int retrieveFrameThreshold(org.w3c.dom.Element element)
    {
        String sameFrameThreshold = element.getAttribute("sameFrameThreshold");
        return Integer.parseInt(sameFrameThreshold);
    }

    private int retrieveHeight(org.w3c.dom.Element element)
    {
        String height = element.getAttribute("height");
        return Integer.parseInt(height);
    }

    private int retrieveWidth(org.w3c.dom.Element element)
    {
        String width = element.getAttribute("width");
        return Integer.parseInt(width);
    }
}
