if ('ontouchstart' in document.documentElement) {
	canvas.addEventListener('touchstart', function(e) {
		handleKeyDown();
	}, false);
} else {
	document.onkeydown = handleKeyDown;
	document.onmousedown = handleKeyDown;
}

var stage,
	canvas,
	hero,
	img = new Image();

function init() {
	canvas = document.createElement('canvas');
	canvas.width = getWidth();
	canvas.height = getHeight();
	document.body.appendChild(canvas);
	stage = new Stage(canvas);

	img.onload = onImageLoaded;
	img.src = 'assets/one.png';
}

function onImageLoaded(e) {
	hero = new Hero(img);
	stage.addChild(hero);
	hero.x=0;
	hero.y=0;
	Ticker.setFPS(30);
	Ticker.addListener(hero.move);
}

// move the hero down by 1px
// and update/render the stage

// whenever a key is pressed then hero's
// position will set to y=0;


function handleKeyDown(e)
{
	var KEYCODE_LEFT = 37;                //usefull keycode
    var KEYCODE_RIGHT = 39;                //usefull keycode	
    //cross browser issues exist
    switch(e.keyCode) {
        case KEYCODE_LEFT: hero.x+=1 ;stage.update();return true;
        case KEYCODE_RIGHT: hero.x-=1 ;stage.update();return true;
    }   
}

init();