import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-carousel-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './carousel-section.component.html',
})
export class CarouselSectionComponent {
  testimonials = [
    {
      name: 'Anna Dimitrova',
      quote: 'CareerPath helped me land my dream internship in Berlin! The smart matching is amazing.',
      role: 'Frontend Intern @ Zalando',
    },
    {
      name: 'Carlos Mendez',
      quote: 'This platform gave me tailored job offers that actually made sense for my CV.',
      role: 'Backend Developer @ Booking.com',
    },
    {
      name: 'Maya Singh',
      quote: 'Finally a job board that understands me. The CV filtering is just brilliant!',
      role: 'AI Intern @ Google Zurich',
    },
    {
      name: 'Ivan Petrov',
      quote: 'Clean UI and strong recommendations. Best job board experience I had!',
      role: 'DevOps Intern @ Spotify',
    },
    {
      name: 'Leila Hamidi',
      quote: 'This site helped me stand out during internship interviews.',
      role: 'UX Designer Intern @ Adidas',
    }
  ];
}
