const Molecule = {
  A: 1,
  B: 2,
  C: 3,
};

const reactionList = [
  {
    Reactant: [Molecule.A, Molecule.B],
    Product: [Molecule.C],
    Activation: 0.05,
    Enthalpy: -0.5,
  },
  {
    Reactant: [Molecule.C],
    Product: [Molecule.A, Molecule.B],
    Activation: 2,
    Enthalpy: 0.5,
  },
];

function deletePair(pair, A, B) {
  if (A) {
    pair.bodyA.label = "X";
  }
  if (B) {
    pair.bodyB.label = "X";
  }
}

function addProduct(products, pos, totalEnergy) {
  var diffAng = 360 / products.length;
  var energy = totalEnergy / products.length;
  print(energy);

  var dir = p5.Vector.random2D();
  print(energy);
  for (let product of products) {
    var velo = createVector(dir.x, dir.y);
    velo.mult(energy);

    particles.push(
      new Particle(
        pos.x + dir.x * 2,
        pos.y + dir.y * 2,
        8,
        product,
        //{ x: 0, y: 0 },
        { x: velo.x, y: velo.y },
      ),
    );

    dir.rotate(diffAng);
  }
}

function callback(event) {
  for (let pair of event.pairs) {
    var totalEnergy = pair.bodyA.speed + pair.bodyB.speed;
    var A = pair.bodyA.label;
    var B = pair.bodyB.label;
    var Apos = pair.bodyA.position;
    var Bpos = pair.bodyB.position;

    for (let reaction of reactionList) {
      if (reaction.Reactant.length == 2) {
        if (
          (A == reaction.Reactant[0] && B == reaction.Reactant[1]) ||
          (B == reaction.Reactant[0] && A == reaction.Reactant[1])
        ) {
          if (totalEnergy > reaction.Activation) {
            deletePair(pair, true, true);
            addProduct(reaction.Product, Apos, totalEnergy - reaction.Enthalpy);
          }
        }
      } else if (reaction.Reactant.length == 1 && A != "wall" && B != "wall") {
        if (A == reaction.Reactant[0]) {
          if (totalEnergy > reaction.Activation) {
            deletePair(pair, true, false);
            addProduct(reaction.Product, Apos, pair.bodyA.speed - reaction.Enthalpy);
          }
        } else if (B == reaction.Reactant[0]) {
          if (totalEnergy > reaction.Activation) {
            deletePair(pair, false, true);
            addProduct(reaction.Product, Bpos, pair.bodyB.speed - reaction.Enthalpy);
          }
        }
      }
    }
  }
}
