package com.example.jakub.snake2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.ref.WeakReference;


/**
 * Created by Jakub on 2015-06-04.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, IGameStateCallback
{
    // in order to avoid memory leaks
    static class GUIThreadHandler extends Handler
    {
        final WeakReference<GameView> m_gameView;

        public GUIThreadHandler(GameView gameView)
        {
            m_gameView = new WeakReference<>(gameView);
        }

        @Override
        public void handleMessage(Message message)
        {
            if (GameViewConstants.GAME_OVER_MESSAGE == message.what)
            {
                GameView gameView = m_gameView.get();
                String gameOver = "\nGAME OVER";
                String scoreInfo = "\n\nYour score is: \n";
                int finalScore = message.arg1;
                gameView.m_gameOverText.setText(gameOver);
                gameView.m_gameOverText.append(scoreInfo);
                String score = Integer.toString(finalScore);
                gameView.m_gameOverText.append(score);
                gameView.showGameOverPopUp();
            }
        }
    }

    @Nullable
    private GameClientBase m_gameClient;
    @Nullable
    private GameActivity m_gameActivity;
    private View m_popUpLayout;
    private View m_gameOverLayout;
    @Nullable
    private PopupWindow m_pausePopUp;
    @Nullable
    private PopupWindow m_gameOverPopUp;
    @Nullable
    private Bitmap m_backgroundGFX;
    private GUIThreadHandler m_GUIThreadHandler;
    private boolean m_disableOnTouch;
    private TextView m_gameOverText;

    public GameView(Context context, GameEngineBase gameEngine)
    {
        super(context);
        m_gameClient = new GameClientBase(gameEngine, this);
        m_GUIThreadHandler = new GUIThreadHandler(this);
        getHolder().addCallback(this);
    }

    public GameView(Context context)
    {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (m_gameClient != null)
        {
            m_gameActivity = retrieveGameActivity();
            m_gameClient.init();
            setInitialBackgroundGFX();
            initPausePopUp();
            initGameOverPopUp();
            showGameScreen();
        }
        else
        {
            System.out.println("ERROR: GameView::surfaceCreated()");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (m_gameClient != null)
        {
            m_gameClient.stopGame();
        }
        else
        {
            System.out.println("ERROR: GameView::surfaceDestroyed()");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!m_disableOnTouch && (event.getAction() == MotionEvent.ACTION_DOWN))
        {
            setBackgroundGFX();
            startGame();
        }

        return true;
    }

    public void gameOver(int finalScore, boolean finishedByUser)
    {
        if (m_gameActivity != null)
        {
            if (!finishedByUser)
            {
                showGameOverPopUp(finalScore);
                m_gameActivity.setFinalScoreForResult(finalScore);
            }
            else
            {
                m_gameActivity.finish();
            }
        }
        else
        {
            System.out.println("ERROR: GameView::initPausePopUp()::m_gameEngine.stopThread()");
        }
    }

    @Override
    public void frameUpdated()
    {
        //retrieve canvas and lock it, we have to pass canvas
        //to render method, SurfaceHolder needs to be synchronized
        //so that to ensure onDraw() won't be invoken in other thread at the same time
        Canvas canvas = null;
        try
        {
            canvas = getHolder().lockCanvas();
            if (canvas != null)
            {
                synchronized (getHolder())
                {
                    onDraw(canvas);
                }
            }
        }
        finally
        {
            // even in case of exception release canvas
            if (canvas != null)
            {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void showGameOverPopUp()
    {
        if ((m_gameActivity != null) && (m_gameOverPopUp != null))
        {
            View gameInterface = m_gameActivity.getGameInterface();
            m_gameOverPopUp.showAtLocation(gameInterface, Gravity.CENTER, 0, GameViewConstants.PAUSE_POPUP_Y_OFFSET);
        }
    }

    private void managePausePopUp()
    {
        if ((m_pausePopUp != null) && (m_gameClient != null) && (m_popUpLayout != null) && (m_gameActivity != null))
        {
            if (!m_pausePopUp.isShowing())
            {
                m_gameClient.pauseGame();
                View gameInterface = m_gameActivity.getGameInterface();
                m_pausePopUp.showAtLocation(gameInterface, Gravity.CENTER, GameViewConstants.PAUSE_POPUP_X_OFFSET, GameViewConstants.PAUSE_POPUP_Y_OFFSET);
            }
            else
            {
                m_pausePopUp.dismiss();
                m_gameClient.unPauseGame();
            }
        }
        else
        {
            System.out.println("ERROR: GameView::managePausePopUp()");
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if ((m_gameClient != null) && (m_backgroundGFX != null))
        {
            canvas.drawBitmap(m_backgroundGFX, 0, 0, null);
            canvas.translate(GameViewConstants.CANVAS_X_TRANSLATE, GameViewConstants.CANVAS_Y_TRANSLATE);
            m_gameClient.drawFrame(canvas);
        }
        // invalidate();
    }

    private void showGameScreen()
    {
        Canvas canvas = getHolder().lockCanvas();
        onDraw(canvas);
        getHolder().unlockCanvasAndPost(canvas);
    }

    private void showGameOverPopUp(int finalScore)
    {
        Message gameOverMessage = m_GUIThreadHandler.obtainMessage(GameViewConstants.GAME_OVER_MESSAGE);
        gameOverMessage.arg1 = finalScore;
        gameOverMessage.sendToTarget();
    }

    private LayoutInflater getInflater()
    {
        Context applicationContext = ResourceManager.getInstance().getApplicationContext();
        return (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void initGameOverPopUp()
    {
        LayoutInflater layoutInflater = getInflater();
        m_gameOverLayout = layoutInflater.inflate(R.layout.game_over_pop_up, null);
        m_gameOverPopUp = new PopupWindow(m_gameOverLayout, GameViewConstants.GAME_OVER_POPUP_WIDTH, GameViewConstants.GAME_OVER_POPUP_HEIGHT, true);
        ImageButton continueButton = (ImageButton) m_gameOverLayout.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v)
              {
                  if (null != m_gameActivity)
                  {
                      ResourceManager.getInstance().playAudioSample("button_click");
                      m_gameActivity.finish();
                  }
              }
        });

        configureTextView();
    }

    private void initPausePopUp()
    {
        LayoutInflater layoutInflater = getInflater();
        m_popUpLayout = layoutInflater.inflate(R.layout.pop_up_game, null);
        m_pausePopUp = new PopupWindow(m_popUpLayout, GameViewConstants.PAUSE_POPUP_WIDTH, GameViewConstants.PAUSE_POPUP_HEIGHT, true);
        initPausePopUpButtons();
         // todo try catch
    }

    private void startGame()
    {
        if (null != m_gameClient)
        {
            initGameButtons();
            m_gameClient.startGame();
            m_disableOnTouch = true;
        }
    }

    private void configureTextView()
    {
        Context context = ResourceManager.getInstance().getApplicationContext();
        AssetManager assetManager = context.getAssets();
        Typeface typeFace = Typeface.createFromAsset(assetManager, "ComickBook_Simple.ttf");
        m_gameOverText = (TextView)m_gameOverLayout.findViewById(R.id.gameOverInfo);
        m_gameOverText.setTypeface(typeFace);
        m_gameOverText.setTextColor(Color.GREEN);
        m_gameOverText.setTextSize(GameViewConstants.GAME_OVER_SCORE_TEXT_SIZE);
    }

    private void setBackgroundGFX()
    {
        m_backgroundGFX = ResourceManager.getInstance().getImage("game_background");
    }

    private void setInitialBackgroundGFX()
    {
        m_backgroundGFX = ResourceManager.getInstance().getImage("game_background_init");
    }

    private void initPausePopUpButtons()
    {
        ImageButton unPauseButton = (ImageButton)m_popUpLayout.findViewById(R.id.unPauseButton);
        unPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ResourceManager.getInstance().playAudioSample("button_click");
                managePausePopUp();
            }
        });

        ImageButton mainMenuButton = (ImageButton)m_popUpLayout.findViewById(R.id.MenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ResourceManager.getInstance().playAudioSample("button_click");
                managePausePopUp();
                gameOver(GameViewConstants.NO_SCORES, true);
            }
        });
    }

    private GameActivity retrieveGameActivity()
    {
        Context context = getContext();
        GameActivity gameActivity = null;

        if (context instanceof GameActivity)
        {
            gameActivity = (GameActivity) context;
        }

        return gameActivity;
    }

    private void initGameButtons()
    {
        initRIGHTButton();
        initLEFTButton();
        initUPButton();
        initDOWNButton();
        initPAUSEButton();
    }

    private void initRIGHTButton()
    {
        if ((m_gameActivity != null) && (m_gameClient != null))
        {
            ImageButton RIGHTButton = (ImageButton) m_gameActivity.findViewById(R.id.rightButton);

            if (null != RIGHTButton)
            {
                RIGHTButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        m_gameClient.setSelectedDirection(Field.Direction.RIGHT);
                        playButtonSound();
                    }
                });
            }
        }
    }

    private void initLEFTButton()
    {
        if ((m_gameActivity != null) && (m_gameClient != null))
        {
            ImageButton LEFTButton = (ImageButton) m_gameActivity.findViewById(R.id.leftButton);

            if (null != LEFTButton)
            {
                LEFTButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        m_gameClient.setSelectedDirection(Field.Direction.LEFT);
                        playButtonSound();
                    }
                });
            }
        }
    }

    private void initUPButton()
    {
        if ((m_gameActivity != null) && (m_gameClient != null))
        {
            ImageButton UPButton = (ImageButton) m_gameActivity.findViewById(R.id.upButton);

            if (null != UPButton)
            {
                UPButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        m_gameClient.setSelectedDirection(Field.Direction.UP);
                        playButtonSound();
                    }
                });
            }
        }
    }

    private void initDOWNButton()
    {
        if ((m_gameActivity != null) && (m_gameClient != null))
        {
            ImageButton DOWNButton = (ImageButton) m_gameActivity.findViewById(R.id.downButton);

            if (null != DOWNButton)
            {
                DOWNButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        m_gameClient.setSelectedDirection(Field.Direction.DOWN);
                        playButtonSound();
                    }
                });
            }
        }
    }

    private void initPAUSEButton()
    {
        if (m_gameActivity != null)
        {
            ImageButton PAUSEButton = (ImageButton) m_gameActivity.findViewById(R.id.pauseButton);

            if (null != PAUSEButton)
            {
                PAUSEButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        managePausePopUp();
                        ResourceManager.getInstance().playAudioSample("button_click");
                    }
                });
            }
        }
    }

    private void playButtonSound()
    {
        ResourceManager resourceManager = ResourceManager.getInstance();
        resourceManager.playAudioSample("button_game");
    }
}
