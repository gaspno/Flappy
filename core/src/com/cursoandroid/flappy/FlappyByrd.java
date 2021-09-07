package com.cursoandroid.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;




import java.util.Random;

public class FlappyByrd extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaro;
	private Texture fundo;
	private Texture canobaixo;
	private Texture canoalto;
	private Texture gameover;

	private Random random;
	private BitmapFont fonte;
	private BitmapFont fonte2;
	private BitmapFont fonte3;
	private Circle passaroCirculo;
	private Rectangle canotop;
	private Rectangle canobot;
	//private ShapeRenderer shapeRenderer;


	private float largura;
	private float altura;
	private int estadojogo=0;//0 jogo não iniciado 1 jogo iniciado 2 game over
	private int aumento=0;
	private int pontos=0;

	private float variation =0;
	private float fallspeed=0;
	private float alturainicial;
	private float positionmovcano;
	private float espaco;
	private float deltatime;
	private float espacoadd;

	private boolean marcacao=false;

	private OrthographicCamera camera;
	private Viewport viewport;
	private final float VIRTUAL_WIDTH=760;
	private final float VIRTUAL_HEIGHT=1024;


	
	@Override
	public void create () {

	    batch=new SpriteBatch();
	    passaroCirculo=new Circle();
	   // canotop=new Rectangle();
	    //canobot=new Rectangle();
	  //  shapeRenderer=new ShapeRenderer();

		passaro=new Texture[3];
		passaro[0]=new Texture("passaro1.png");
		passaro[1]=new Texture("passaro2.png");
		passaro[2]=new Texture("passaro3.png");
		canoalto=new Texture("cano_topo.png");
        canobaixo=new Texture("cano_baixo.png");

        gameover=new Texture("game_over.png");


        random=new Random();
        fonte =new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(4);
        fonte2 =new BitmapFont();
        fonte2.setColor(Color.WHITE);
        fonte2.getData().setScale(4);
        fonte3=new BitmapFont();
        fonte3.setColor(Color.BLACK);
        fonte3.getData().setScale(3);


	    fundo=new Texture("fundo.png");
	    largura=VIRTUAL_WIDTH;
		altura=VIRTUAL_HEIGHT;
		alturainicial=altura/2;
		positionmovcano=largura;
		espaco=400;

		camera=new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT/2,0);
		viewport=new StretchViewport(VIRTUAL_WIDTH,VIRTUAL_HEIGHT,camera);



	}

	@Override
	public void render () {
		camera.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		deltatime=Gdx.graphics.getDeltaTime()*5;
		variation+=deltatime;
		if(variation>2)
			variation=0;
		if(estadojogo==0){
			if(Gdx.input.justTouched()){
				estadojogo=1;
			}
		}
		else if(estadojogo==1){



		positionmovcano-=deltatime*(100+aumento);
		fallspeed++;



		if(Gdx.input.justTouched())
			fallspeed=-15;

		if(alturainicial>0||fallspeed<0)
			alturainicial-=fallspeed;
		if(positionmovcano<-canobaixo.getWidth()) {
			positionmovcano = largura;
			espacoadd = random.nextInt((int) espaco) - espaco/2;
			if(espaco>=150)
			espaco--;
			if(aumento<350)
			aumento++;
			marcacao=false;
		}

		if(positionmovcano<120){
			if(!marcacao) {
				pontos++;
				marcacao=true;
			}
		}

        }
        else{
            if(Gdx.input.justTouched()){
                estadojogo=1;
                pontos=0;
                aumento=0;
                espaco=400;
                positionmovcano=largura;
                fallspeed=0;
                alturainicial=altura/2;


            }

		}
		batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(fundo,0,0,largura,altura);

        batch.draw(canoalto,positionmovcano,altura/2+espaco/2+espacoadd/2,canoalto.getWidth(),altura/2+espaco/2+espacoadd/2);

        batch.draw(canobaixo,positionmovcano,0,canobaixo.getWidth(),altura/2-espaco/2-espacoadd/2);

        batch.draw(passaro[(int) variation],120,alturainicial);

        fonte.draw(batch,String.valueOf("Pontução : "+pontos),largura/2-100,altura-50);
        fonte2.draw(batch,String.valueOf("Velocidade :"+(100+aumento)),largura/2-100,altura-200);

		if(estadojogo==2){
			batch.draw(gameover,largura/2-gameover.getWidth()/2,altura/2);
			fonte3.draw(batch,String.valueOf("Toque para reiniciar o jogo\n e continuar se divertindo"),largura/2-gameover.getWidth()/2-10,altura/2-gameover.getHeight()/2);

		}

        batch.end();

        passaroCirculo.set(120+passaro[0].getWidth()/2,alturainicial+passaro[0].getHeight()/2,passaro[0].getWidth()/2);

        canobot=new Rectangle(positionmovcano,0,canobaixo.getWidth(),altura/2-espaco/2-espacoadd/2);

        canotop=new Rectangle(positionmovcano,altura/2+espaco/2+espacoadd/2,canoalto.getWidth(),altura/2+espaco/2+espacoadd/2);



      /*  shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.circle(passaroCirculo.x,passaroCirculo.y,passaroCirculo.radius);
        shapeRenderer.rect(canobot.x,canobot.y,canobot.getWidth(),canobot.height);
        shapeRenderer.rect(canotop.x,canotop.y,canotop.getWidth(),canotop.getHeight());

        shapeRenderer.setColor(Color.RED);

        shapeRenderer.end();*/
        if(Intersector.overlaps(passaroCirculo,canobot)||Intersector.overlaps(passaroCirculo,canotop)||alturainicial<=0||alturainicial>=altura){
            estadojogo=2;
        }


    }

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width,height);
	}
}
