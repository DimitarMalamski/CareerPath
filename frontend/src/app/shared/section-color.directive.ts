import { Directive, ElementRef, Input, OnDestroy, OnInit, inject } from '@angular/core';
import { PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Directive({
  selector: '[appSectionColor]',
  standalone: true,
})

export class SectionColorDirective implements OnInit, OnDestroy {
  @Input('appSectionColor') theme = '';
  private obs?: IntersectionObserver;
  private platformId = inject(PLATFORM_ID);

  constructor(private el: ElementRef<HTMLElement>) { }

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) return;

    console.log('[appSectionColor] Initialized for section:', this.theme);

    this.obs = new IntersectionObserver(
      entries => {
        for (const e of entries) {
          if (e.isIntersecting && e.intersectionRatio >= 0.6) {
            console.log('[appSectionColor] Switching theme to:', this.theme);
            document.documentElement.setAttribute('data-theme', this.theme);
          }
        }
      },
      { threshold: [0.6] }
    );
    this.obs.observe(this.el.nativeElement);
  }

  ngOnDestroy() {
    this.obs?.disconnect();
  }
}
