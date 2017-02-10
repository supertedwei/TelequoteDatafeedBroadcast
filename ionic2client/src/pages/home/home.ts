import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';

import * as io from "socket.io-client";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  socket: SocketIOClient.Socket;

  constructor(public navCtrl: NavController) {
    this.socket = io("http://localhost:3000");
    this.socket.on('connect', () => {
      console.log("on connect")
    });
    this.socket.on('event', function(data){
        console.log("on event");
    });
    this.socket.on('disconnect', function(){
        console.log("on disconnect");
    });
  }
  

}
