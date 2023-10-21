import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const BoxContainer = styled.div`
  display: flex;
  justify-content: center;
  flex-direction: column;
  border: 0.1rem solid ${palette.divider};
  border-radius: 1rem;
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};

  ${breakpoints.large} {
    flex-direction: row-reverse;
    justify-content: space-between;
  }
`

export const ImgWrap = styled.div`
  width: 30rem;
  height: 30rem;
  display: flex;
  justify-content: center;
  align-items: center;
  width: clamp(100%, 10vw, 30rem);
  height: clamp(15rem, 10vw, 30rem);

  & > img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 1rem 1rem 0 0;
  }

  ${breakpoints.large} {
    width: clamp(40rem, 50vw, 50%);
    height: clamp(30rem, 50vw, 30rem);

    & > img {
      border-radius: 0 1rem 1rem 0;
    }
  }
`

export const TextWrap = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 2rem;

  & > div {
    display: flex;
    justify-content: space-between;
  }

  ${breakpoints.large} {
    padding: 1rem 4rem;
  }
`

export const LikeButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.4rem;
`

export const Title = styled.h2`
  margin: 0.1rem 0;
  font-size: ${typography.mobile.mainTitle};
  font-weight: 800;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
  }
`

export const Content = styled.p`
  margin: 1rem 0;
  font-size: ${typography.mobile.content};
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  word-wrap: break-word;
  overflow-wrap: break-word;

  ${breakpoints.large} {
    width: 100%;
    font-size: ${typography.web.content};
    max-width: 45rem;
    -webkit-line-clamp: 3;
  }
`
