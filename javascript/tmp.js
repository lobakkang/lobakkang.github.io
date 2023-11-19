
function deletePair(pair) {
  pair.bodyA.label = "X";
  pair.bodyB.label = "X";
}

function rotateVec(vec) {
  return { x: vec.y, y: -vec.x };
}

function callback(event) {
  for (let pair of event.pairs) {
    var totalEnergy = pair.bodyA.speed + pair.bodyB.speed;
    var A = pair.bodyA.label;
    var B = pair.bodyB.label;
    var Avelo = pair.bodyA.velocity;
    var Bvelo = pair.bodyB.velocity;
    var velo = { x: (Avelo.x + Bvelo.x) / 2, y: (Avelo.y + Bvelo.y) / 2 };
    var spd = (pair.bodyA.speed + pair.bodyB.speed) / 2;
    var offset = 10;

    if (A == Molecule.NH3 && B == Molecule.NH3) {
      deletePair(pair);

      spd = sqrt(sq(spd) / 32);

      particles.push(
        new Particle(
          pair.bodyA.position.x - offset,
          pair.bodyA.position.y - offset,
          8,
          Molecule.N2,
          { x: -spd, y: -spd },
        ),
      );
      velo = rotateVec(velo);
      particles.push(
        new Particle(
          pair.bodyA.position.x + offset,
          pair.bodyA.position.y - offset,
          8,
          Molecule.H2,
          { x: spd, y: -spd },
        ),
      );
      velo = rotateVec(velo);
      particles.push(
        new Particle(
          pair.bodyA.position.x - offset,
          pair.bodyA.position.y + offset,
          8,
          Molecule.H2,
          { x: -spd, y: spd },
        ),
      );
      velo = rotateVec(velo);
      particles.push(
        new Particle(
          pair.bodyA.position.x + offset,
          pair.bodyA.position.y + offset,
          8,
          Molecule.H2,
          { x: spd, y: spd },
        ),
      );
    } else if (
      (A == Molecule.H2 && B == Molecule.N2) ||
      (B == Molecule.H2 && A == Molecule.N2)
    ) {
      deletePair(pair);
      particles.push(
        new Particle(
          pair.bodyA.position.x,
          pair.bodyA.position.y,
          8,
          Molecule.INT_HN,
          { x: spd * 2, y: 0 },
        ),
      );
    } else if (A == Molecule.H2 && B == Molecule.H2) {
      deletePair(pair);
      particles.push(
        new Particle(
          pair.bodyA.position.x,
          pair.bodyA.position.y,
          8,
          Molecule.INT_HH,
          { x: 0, y: spd * 2 },
        ),
      );
    } else if (
      (A == Molecule.INT_HN && B == Molecule.INT_HH) ||
      (A == Molecule.INT_HH && B == Molecule.INT_HN)
    ) {
      deletePair(pair);

      particles.push(
        new Particle(
          pair.bodyA.position.x - offset,
          pair.bodyA.position.y - offset,
          8,
          Molecule.NH3,
          { x: -spd, y: 0 },
        ),
      );
      particles.push(
        new Particle(
          pair.bodyA.position.x + offset,
          pair.bodyA.position.y + offset,
          8,
          Molecule.NH3,
          { x: spd, y: 0 },
        ),
      );
    } else if (A == Molecule.INT_HN) {
      pair.bodyA.label = "X";
      spd = pair.bodyA.speed / 2;
      spd = sqrt(sq(spd) / 2);

      particles.push(
        new Particle(
          pair.bodyA.position.x - offset,
          pair.bodyA.position.y - offset,
          8,
          Molecule.H2,
          { x: -spd, y: -spd },
        ),
      );
      particles.push(
        new Particle(
          pair.bodyA.position.x + offset,
          pair.bodyA.position.y + offset,
          8,
          Molecule.N2,
          { x: spd, y: spd },
        ),
      );
    } else if (B == Molecule.INT_HN) {
      pair.bodyB.label = "X";
      spd = pair.bodyB.speed / 2;
      spd = sqrt(sq(spd) / 2);

      particles.push(
        new Particle(
          pair.bodyB.position.x - offset,
          pair.bodyB.position.y - offset,
          8,
          Molecule.H2,
          { x: -spd, y: -spd },
        ),
      );
      particles.push(
        new Particle(
          pair.bodyB.position.x + offset,
          pair.bodyB.position.y + offset,
          8,
          Molecule.N2,
          { x: spd, y: spd },
        ),
      );
    } else if (A == Molecule.INT_HH) {
      pair.bodyA.label = "X";
      spd = pair.bodyA.speed / 2;
      spd = sqrt(sq(spd) / 2);

      particles.push(
        new Particle(
          pair.bodyA.position.x - offset,
          pair.bodyA.position.y - offset,
          8,
          Molecule.H2,
          { x: -spd, y: -spd },
        ),
      );
      particles.push(
        new Particle(
          pair.bodyA.position.x + offset,
          pair.bodyA.position.y + offset,
          8,
          Molecule.H2,
          { x: spd, y: spd },
        ),
      );
    } else if (B == Molecule.INT_HH) {
      pair.bodyB.label = "X";
      spd = pair.bodyB.speed / 2;
      spd = sqrt(sq(spd) / 2);

      particles.push(
        new Particle(
          pair.bodyB.position.x - offset,
          pair.bodyB.position.y - offset,
          8,
          Molecule.H2,
          { x: -spd, y: -spd },
        ),
      );
      particles.push(
        new Particle(
          pair.bodyB.position.x + offset,
          pair.bodyB.position.y + offset,
          8,
          Molecule.H2,
          { x: spd, y: spd },
        ),
      );
    }
  }
}
