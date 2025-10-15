import { Component, OnInit, OnDestroy, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-carousel-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './carousel-section.component.html',
})
export class CarouselSectionComponent implements OnInit, OnDestroy {
  originalTestimonials = [
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
  ];

  testimonials: any[] = [];
  currentIndex = 3; // First real slide starts here (after 3 prepended clones)
  intervalId: any;
  transitionDuration = 700;
  isTransitioning = false;

  constructor(private renderer: Renderer2) {}

  ngOnInit(): void {
    // Clone last 3 and first 3 to simulate infinite scroll
    this.testimonials = [
      ...this.originalTestimonials.slice(-3),
      ...this.originalTestimonials,
      ...this.originalTestimonials.slice(0, 3),
    ];

    this.intervalId = setInterval(() => {
      this.next();
    }, 3000);
  }

  next(): void {
    if (this.isTransitioning) return;
    this.isTransitioning = true;

    this.currentIndex++;

    setTimeout(() => {
      if (this.currentIndex >= this.testimonials.length - 3) {
        this.disableTransitionTemporarily();
        this.currentIndex = 3;
      }
      this.isTransitioning = false;
    }, this.transitionDuration + 50);
  }

  disableTransitionTemporarily(): void {
    const track = document.querySelector('.carousel-track') as HTMLElement;
    if (track) {
      this.renderer.setStyle(track, 'transition', 'none');
      track.style.transform = `translateX(-${this.currentIndex * (100 / 3)}%)`;
      setTimeout(() => {
        this.renderer.removeStyle(track, 'transition');
      });
    }
  }

  ngOnDestroy(): void {
    clearInterval(this.intervalId);
  }
}
