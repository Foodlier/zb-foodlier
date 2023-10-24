import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const Container = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  margin: 0 0 1% 0;
  padding: 1rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;

  ${breakpoints.large} {
    flex-direction: column;
    width: 31.4%;
    padding: 0;
    margin-right: 1.3rem;
    margin-bottom: 3rem;
  }
`

export const Button = styled.button`
  display: flex;
  flex-direction: row;
  width: 100%;

  ${breakpoints.large} {
    flex-direction: column;
  }
`

export const Content = styled.div`
  width: 70%;
  display: flex;
  flex-direction: column;
  margin: 1rem 0 0 1rem;

  ${breakpoints.large} {
    width: 90%;
    padding: 3%;
  }
`

export const Title = styled.span`
  align-self: flex-start;
  font-size: ${typography.mobile.content};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const Introduce = styled.span`
  width: 90%;
  font-size: ${typography.mobile.desc};
  color: ${palette.textSecondary};
  margin: 0.6rem 0;
  text-align: left;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;

  ${breakpoints.large} {
    width: 80%;
    font-size: ${typography.web.desc};
  }
`
export const LikeButton = styled.button`
  position: absolute;
  bottom: 1rem;
  right: 1rem;
  padding: 1rem;
  display: flex;
  align-items: center;
  justify-content: flex-end;
`
export const LikeCount = styled.span`
  font-size: ${typography.mobile.desc};
  color: ${palette.textPrimary};
  margin-left: 0.5rem;
  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const Image = styled.img`
  width: 30%;
  height: 10rem;
  object-fit: cover;
  margin-right: 1rem;
  border-radius: 1rem;
  border-bottom: 1px solid ${palette.divider};

  ${breakpoints.large} {
    width: 100%;
    height: 20rem;
    margin: 0;
    border-radius: 1rem 1rem 0 0;
  }
`

export const FlexRowJustiBet = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
`

export const FlexRow = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
`
export const WrapButton = styled.div`
  width: 100%;
  display: flex;
  /* border-top: 1px solid ${palette.divider}; */
`

export const IconButton = styled.button`
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
`
