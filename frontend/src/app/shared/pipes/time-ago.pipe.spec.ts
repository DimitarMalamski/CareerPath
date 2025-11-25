import { TimeAgoPipe } from './time-ago.pipe';
import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest';

describe('TimeAgoPipe', () => {
  let pipe: TimeAgoPipe;

  beforeAll(() => {
    pipe = new TimeAgoPipe();
  });

  afterAll(() => {
    vi.restoreAllMocks();
  });

  it('should return empty string if no value is provided', () => {
    expect(pipe.transform('')).toBe('');
    expect(pipe.transform(null as unknown as string)).toBe('');
    expect(pipe.transform(undefined as unknown as string)).toBe('');
  });

  it('should return "Just now" when less than 1 minute ago', () => {
    const now = new Date();
    const justNow = new Date(now.getTime() - 10 * 1000); // 10 seconds ago

    expect(pipe.transform(justNow.toISOString())).toBe('Just now');
  });

  it('should return minutes ago', () => {
    const now = new Date();
    const minutesAgo = new Date(now.getTime() - 5 * 60 * 1000);

    expect(pipe.transform(minutesAgo.toISOString())).toBe('5 minutes ago');
  });

  it('should return hours ago', () => {
    const now = new Date();
    const hoursAgo = new Date(now.getTime() - 2 * 3600 * 1000);

    expect(pipe.transform(hoursAgo.toISOString())).toBe('2 hours ago');
  });

  it('should return days ago', () => {
    const now = new Date();
    const daysAgo = new Date(now.getTime() - 3 * 86400 * 1000);

    expect(pipe.transform(daysAgo.toISOString())).toBe('3 days ago');
  });
});
