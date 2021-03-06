import { AuthService } from './../../services/auth.service';
import { Router } from '@angular/router';
import { FlashMessagesService } from 'angular2-flash-messages';
import { PollService } from './../../services/poll.service';
import { Component, OnInit } from '@angular/core';
import { Poll } from '../../models/poll.model';

@Component({
  selector: 'app-my-polls',
  templateUrl: './my-polls.component.html',
  styleUrls: ['./my-polls.component.css']
})
export class MyPollsComponent implements OnInit {

  constructor(private pollService: PollService,
    private flashMessagesService: FlashMessagesService,
    private router: Router,
    private authService: AuthService) { }

  polls: Poll[] = [];

  ngOnInit() {
    this.loadMyPolls();
  }

  deletePoll(id) {
    this.pollService.deletePoll(id)
      .subscribe(success => {
        this.loadMyPolls();
      }, error => {
        console.log(error);
      });
  }

  loadMyPolls() {
    let username = this.authService.getLoggedInUser();
    this.pollService.getPollsForUser(username).subscribe(polls => {
      this.polls = polls;
    }, error => {
      console.log(error);
    });
  }
}
