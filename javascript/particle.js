var Engine = Matter.Engine,
  World = Matter.World,
  Events = Matter.Events,
  Bodies = Matter.Bodies,
  Body = Matter.Body;

class Boundary {
  constructor(x, y, w, h) {
    let options = {
      friction: 0,
      restitution: 1,
      isStatic: true,
      friction: 0,
      frictionAir: 0,
      frictionStatic: 0,
      label: "wall",
    };
    this.body = Bodies.rectangle(x, y, w, h, options);
    this.w = w;
    this.h = h;
    World.add(world, this.body);
  }

  show() {
    let pos = this.body.position;
    let angle = this.body.angle;

    push();
    translate(pos.x, pos.y);
    rotate(angle);
    rectMode(CENTER);
    strokeWeight(1);
    noStroke();
    fill(0, 0, 0);
    rect(0, 0, this.w, this.h);
    pop();
  }
}

var area = 800;
class Piston {
  constructor(x, y, w, h) {
    let options = {
      friction: 0,
      restitution: 1,
      isStatic: true,
      friction: 0,
      frictionAir: 0,
      frictionStatic: 0,
      label: "piston",
    };
    this.body = Bodies.rectangle(x, y, w, h, options);
    this.w = w;
    this.h = h;
    World.add(world, this.body);
  }

  show() {
    let pos = this.body.position;
    let angle = this.body.angle;

    push();
    fill(100, 100, 100);
    area = 800 - this.body.position.y;
    if (keyIsDown(LEFT_ARROW)) {
      fill(0, 100, 100);
      Body.setPosition(this.body, {
        x: this.body.position.x,
        y: this.body.position.y + 1,
      });
    }

    translate(pos.x, pos.y);
    rotate(angle);
    rectMode(CENTER);
    strokeWeight(1);
    noStroke();
    rect(0, 0, this.w, this.h);
    pop();
  }
}

class Particle {
  constructor(x, y, r, type, velo) {
    let options = {
      friction: 0.0,
      restitution: 1.0,
      inertia: Infinity,
      friction: 0,
      frictionAir: 0,
      frictionStatic: 0,
    };
    this.body = Bodies.circle(x, y, r, options);
    Body.setVelocity(this.body, velo);
    //this.body.setVelocity({x: random(1), y: random(1)});
    this.r = r;
    this.body.label = type;
    World.add(world, this.body);
  }

  show() {
    let pos = this.body.position;
    let angle = this.body.angle;

    push();
    translate(pos.x, pos.y);
    rotate(angle);
    rectMode(CENTER);
    strokeWeight(1);
    stroke(255);
    fill(255, 255, 255);
    if (this.body.label == Molecule.A) {
      fill(255, 0, 0);
    } else if (this.body.label == Molecule.B) {
      fill(0, 255, 0);
    } else if (this.body.label == Molecule.C) {
      fill(0, 0, 255);
    }
    ellipse(0, 0, this.r * 2);
    pop();

    if (keyIsDown(RIGHT_ARROW)) {
      this.body.frictionAir = 0.01;
    } else {
      this.body.frictionAir = 0;
    }
    if (keyIsDown(SHIFT)) {
      Body.setSpeed(this.body, this.body.speed * 1.01);
    }
  }
}

let engine;
let world;
let particles = [];
let grounds = [];
let mConstraint;

let canvas;
let sizes = [5, 10, 20, 30, 40];

function setup() {
  canvas = createCanvas(800, 1000);
  engine = Engine.create({ gravity: { y: 0 } });
  world = engine.world;
  //  Engine.run(engine);
  grounds.push(new Boundary(0, height / 2, 100, height));
  grounds.push(new Boundary(width, height / 2, 100, height));
  grounds.push(new Piston(400, 0, width, 100));
  grounds.push(new Boundary(400, height, width, 100));
  World.add(world, grounds);

  for (var i = 0; i < 400; i++) {
    particles.push(
      new Particle((i / 20) * 35, (i % 20) * 35, 8, Molecule.C, {
        x: random(1.5),
        y: random(1.5),
      }),
    );
  }
  /*particles.push(
    new Particle(70, 300, 8, Molecule.A, {
      x: 5,
      y: 0,
    }),
  );
  particles.push(
    new Particle(700, 300, 8, Molecule.C, {
      x: -5,
      y: 0,
    }),
  );*/

  Events.on(engine, "collisionEnd", callback);
}

function draw() {
  if (keyIsDown(RIGHT_ARROW)) {
    background(200, 200, 255);
  } else if (keyIsDown(SHIFT)) {
    background(255, 200, 200);
  } else {
    background(255);
  }
  Engine.update(engine);
  var totalEnergy = 0;
  var totalA = 0;
  var totalB = 0;
  var totalC = 0;

  var i = 0;
  for (let particle of particles) {
    if (particle.body.label == "X") {
      World.remove(world, particle.body);
      delete particle;
      particles.splice(i, 1);
    } else if (particle.body.label == Molecule.A) {
      totalA++;
    } else if (particle.body.label == Molecule.B) {
      totalB++;
    } else if (particle.body.label == Molecule.C) {
      totalC++;
    }
    particle.show();
    totalEnergy = totalEnergy + particle.body.speed;
    i++;
  }
  for (let ground of grounds) {
    ground.show();
  }
  var kC = (totalC / area) / ((totalA / area) * (totalB / area));
  text(
    "Total energy: " + totalEnergy + "\nTotal A: " + totalA +
      "\nTotal B: " + totalB + "\nTotal C: " + totalC + "\nArea: " + area +
      "\nKc: " + kC,
    50,
    850,
  );
}
