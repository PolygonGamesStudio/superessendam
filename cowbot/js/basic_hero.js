(function (window) {
	function Hero(image) {
		this.initialize(image);
	}
	Hero.prototype = new Bitmap();

	// save the original initialize-method so it won't be gone after
	// overwriting it
	Hero.prototype.Bitmap_initialize = Hero.prototype.initialize;

	// initialize the object
	Hero.prototype.initialize = function (image) {
		this.Bitmap_initialize(image);
		this.name = 'Hero';
		this.snapToPixel = true;
	}

	// Hero.prototype.move = function(side) {
	// 	if (side = 'left') {
	// 		this.x = 50;	
	// 	}
	// 	else {
	// 		this.x -= 1;
	// 	}
	// }	

	window.Hero = Hero;
} (window));