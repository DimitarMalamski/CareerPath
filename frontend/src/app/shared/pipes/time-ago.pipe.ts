import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'timeAgo',
  standalone: true
})

export class TimeAgoPipe implements PipeTransform {

  transform(value: string): string {
    if (!value) return '';

    const date = new Date(value);
    const now = new Date();
    const second = Math.floor((now.getTime() - date.getTime()) / 1000);

    const intervals: Record<string, number> = {
      year: 31536000,
      month: 2592000,
      week: 604800,
      day: 86400,
      hour: 3600,
      minute: 60,
    };

    for (const unit of Object.keys(intervals)) {
      const internal = Math.floor(second / intervals[unit]);
      if (internal >= 1) {
        return `${internal} ${unit}${internal > 1 ? 's' : ''} ago`;
      }
    }

    return 'Just now';
  }
}
