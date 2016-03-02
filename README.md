Representing Verbs as Argument Concepts
====================================
## Abstract
Verbs play an important role in the understanding of natural language text. This paper studies the problem of abstracting the subject and object arguments of a verb into a set of noun concepts, known as the “argument concepts”. This set of concepts, whose size is parameterized, represents the finegrained semantics of a verb. For example, the object of “enjoy” can be abstracted into time, hobby and event, etc. We present a novel framework to automatically infer human readable and machine computable action concepts with high accuracy.

## Code
1. The directory `SP` is the code for generating verb argument concepts based on `Selectional Preference` by Philip Resnik.
2. The directory `Action` is the code for generating verb argument concepts based on algorithm proposed in our paper.

## Dependency
1. The taxonomies used in both algorithm are `WordNet` from Princeton University and `Probase` from MSRA.
2. The training corpus used in both algorithm is `google syntatic n-gram`.

## Publication
In the preceeding of AAAI 2016, link: <http://www.cs.sjtu.edu.cn/~kzhu/papers/kzhu-action.pdf>

## Slide
The talk about this project in “Frontiers in Knowledge Graphs 2015” is at <http://pangolulu.github.io/2016/01/09/action/>. You can rely on this slide to know more about our ideas and approaches.

## Demo
A website demo of our project is at <http://202.120.38.146/~gongyu/action/>, where can browse the verbs' argument concepts in the browser.
