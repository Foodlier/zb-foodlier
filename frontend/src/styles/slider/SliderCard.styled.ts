import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const BoxContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 35rem;
  border: 0.1rem solid ${palette.divider};
  border-radius: 2rem;
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};

  ${breakpoints.large} {
    height: 30rem;
    flex-direction: row-reverse;
    justify-content: space-between;
  }
`

export const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 2rem 2rem 0 0;

  ${breakpoints.large} {
    width: 40%;
    border-radius: 0 2rem 2rem 0;
  }
`

export const TextWrap = styled.div`
  display: flex;

  flex-direction: column;
  justify-content: center;
  padding: 1rem 3rem;

  ${breakpoints.large} {
    width: 60%;
  }
`

export const FlexWrap = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
`

export const Like = styled.button`
  background: none;
  border: none;
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: ${typography.mobile.content};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const Title = styled.h2`
  margin: 0.1rem 0;
  font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const Content = styled.p`
  margin: 1rem 0;
  font-size: ${typography.mobile.content};
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
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
